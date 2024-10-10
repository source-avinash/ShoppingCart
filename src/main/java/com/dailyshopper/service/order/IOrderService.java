package com.dailyshopper.service.order;

import com.dailyshopper.dto.OrderDto;
import com.dailyshopper.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);


    OrderDto convertToDto(Order order);

    List<OrderDto> getUserOrders(Long userId);
}
