package bilkom.uz.chemical.service.orders;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.orders.OrderDto;
import bilkom.uz.chemical.dto.orders.OrderItemDto;
import bilkom.uz.chemical.entity.clients.Client;
import bilkom.uz.chemical.entity.orders.Order;
import bilkom.uz.chemical.entity.orders.OrderItem;
import bilkom.uz.chemical.entity.orders.OrderState;
import bilkom.uz.chemical.entity.orders.OrderStatus;
import bilkom.uz.chemical.entity.warehouse.Warehouse;
import bilkom.uz.chemical.repository.clients.ClientRepository;
import bilkom.uz.chemical.repository.orders.OrderRepository;
import bilkom.uz.chemical.repository.warehouse.WarehouseRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final SecurityUtils securityUtils;

    public Result getAll() {
        return new Result("OK", true, orderRepository.findAll());
    }

    public Result getById(Long id) {
        return orderRepository.findById(id)
                .map(o -> new Result("OK", true, o))
                .orElse(new Result("Buyurtma topilmadi", false));
    }

    public Result getByClient(Long clientId) {
        return new Result("OK", true, orderRepository.findAllByClientId(clientId));
    }

    public Result getByStatus(OrderStatus status) {
        return new Result("OK", true, orderRepository.findAllByStatus(status));
    }

    @Transactional
    public Result add(OrderDto dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            return new Result("Buyurtmada kamida bitta qator bo'lishi kerak", false);
        }
        Optional<Client> clientOpt = clientRepository.findById(dto.getClientId());
        if (clientOpt.isEmpty()) {
            return new Result("Mijoz topilmadi", false);
        }

        Map<Long, Warehouse> warehouseCache = new HashMap<>();
        String stockError = validateStock(dto.getItems(), warehouseCache, new HashMap<>());
        if (stockError != null) {
            return new Result(stockError, false);
        }

        Order order = new Order();
        order.setClient(clientOpt.get());
        order.setOrderDescription(dto.getOrderDescription());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : OrderStatus.PENDING);
        order.setState(dto.getState() != null ? dto.getState() : OrderState.ACTIVE);
        securityUtils.getCurrentUser().ifPresent(order::setCreatedBy);

        BigDecimal total = applyItems(order, dto.getItems(), warehouseCache);
        order.setTotalAmount(total);

        orderRepository.save(order);
        return new Result("Buyurtma qo'shildi", true);
    }

    @Transactional
    public Result edit(Long id, OrderDto dto) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return new Result("Buyurtma topilmadi", false);
        }
        Order order = orderOpt.get();

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return new Result("Bekor qilingan buyurtmani tahrirlab bo'lmaydi", false);
        }

        if (dto.getStatus() == OrderStatus.CANCELLED) {
            restockItems(order.getItems());
            order.setStatus(OrderStatus.CANCELLED);
            if (dto.getState() != null) order.setState(dto.getState());
            orderRepository.save(order);
            return new Result("Buyurtma bekor qilindi, ombor zaxirasi qaytarildi", true);
        }

        if (dto.getClientId() != null) {
            Optional<Client> clientOpt = clientRepository.findById(dto.getClientId());
            if (clientOpt.isEmpty()) {
                return new Result("Mijoz topilmadi", false);
            }
            order.setClient(clientOpt.get());
        }
        order.setOrderDescription(dto.getOrderDescription());

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            // "Effektiv mavjudlik" = joriy residual + shu order o'zi ushlab turgan miqdor
            // (xuddi shu ombor yozuvi qayta tanlanganda noto'g'ri "yetarli emas" xatosini oldini olish uchun)
            Map<Long, Double> heldByThisOrder = new HashMap<>();
            for (OrderItem oldItem : order.getItems()) {
                heldByThisOrder.merge(oldItem.getWarehouse().getId(), oldItem.getQuantity(), Double::sum);
            }

            Map<Long, Warehouse> warehouseCache = new HashMap<>();
            String stockError = validateStock(dto.getItems(), warehouseCache, heldByThisOrder);
            if (stockError != null) {
                return new Result(stockError, false);
            }

            restockItems(order.getItems());
            order.getItems().clear();
            BigDecimal total = applyItems(order, dto.getItems(), warehouseCache);
            order.setTotalAmount(total);
        }

        if (dto.getState() != null) order.setState(dto.getState());

        orderRepository.save(order);
        return new Result("Buyurtma tahrirlandi", true);
    }

    @Transactional
    public Result delete(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return new Result("Buyurtma topilmadi", false);
        }
        Order order = orderOpt.get();
        if (order.getStatus() != OrderStatus.CANCELLED) {
            restockItems(order.getItems());
        }
        orderRepository.deleteById(id);
        return new Result("Buyurtma o'chirildi", true);
    }

    /**
     * Har bir warehouse uchun so'ralgan miqdorlarni yig'adi va mavjud (joriy residual + heldByThisOrder)
     * zaxiradan oshib ketmasligini tekshiradi. Xato bo'lsa xabar matnini, aks holda null qaytaradi.
     */
    private String validateStock(java.util.List<OrderItemDto> items,
                                  Map<Long, Warehouse> warehouseCache,
                                  Map<Long, Double> heldByThisOrder) {
        Map<Long, Double> requested = new HashMap<>();
        for (OrderItemDto itemDto : items) {
            Warehouse w = warehouseCache.computeIfAbsent(itemDto.getWarehouseId(),
                    wid -> warehouseRepository.findById(wid).orElse(null));
            if (w == null) {
                return "Ombor yozuvi topilmadi: " + itemDto.getWarehouseId();
            }
            double reqSoFar = requested.merge(w.getId(), itemDto.getQuantity(), Double::sum);
            double currentResidual = w.getResidual() != null ? w.getResidual() : 0.0;
            double available = currentResidual + heldByThisOrder.getOrDefault(w.getId(), 0.0);
            if (reqSoFar > available) {
                return "Omborda yetarli mahsulot yo'q: " + resolveProductName(w)
                        + ", mavjud: " + available + " " + w.getMeasure();
            }
        }
        return null;
    }

    /** Validatsiyadan o'tgan itemlarni order'ga bog'laydi va warehouse residual'ini kamaytiradi. */
    private BigDecimal applyItems(Order order, java.util.List<OrderItemDto> items, Map<Long, Warehouse> warehouseCache) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDto itemDto : items) {
            Warehouse w = warehouseCache.get(itemDto.getWarehouseId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setWarehouse(w);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());
            order.getItems().add(item);

            w.setResidual((w.getResidual() != null ? w.getResidual() : 0.0) - itemDto.getQuantity());
            warehouseRepository.save(w);

            total = total.add(itemDto.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }
        return total;
    }

    private void restockItems(Iterable<OrderItem> items) {
        for (OrderItem item : items) {
            Warehouse w = item.getWarehouse();
            w.setResidual((w.getResidual() != null ? w.getResidual() : 0.0) + item.getQuantity());
            warehouseRepository.save(w);
        }
    }

    private String resolveProductName(Warehouse w) {
        if (w.getProduct() != null) return w.getProduct().getProductName();
        if (w.getPurchase() != null) return w.getPurchase().getProductName();
        return "Noma'lum mahsulot";
    }
}
