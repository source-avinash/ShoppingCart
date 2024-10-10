package com.dailyshopper.service.order;

import com.dailyshopper.dto.OrderDto;
import com.dailyshopper.enums.OrderStatus;
import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Cart;
import com.dailyshopper.model.Order;
import com.dailyshopper.model.OrderItem;
import com.dailyshopper.model.Product;
import com.dailyshopper.repository.OrderRepository;
import com.dailyshopper.repository.ProductRepository;
import com.dailyshopper.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItem(order, cart);
        order.setOrderItem(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;

    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }


    private List<OrderItem> createOrderItem(Order order, Cart cart) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice());
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){

        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }


    @Override
    public OrderDto getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {

        List<Order> order = orderRepository.findByUserId(userId);
        return order.stream().map(this :: convertToDto).toList();
    }
}
