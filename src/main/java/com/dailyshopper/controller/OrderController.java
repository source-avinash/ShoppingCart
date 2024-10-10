package com.dailyshopper.controller;


import com.dailyshopper.dto.OrderDto;
import com.dailyshopper.model.Order;
import com.dailyshopper.response.ApiResponse;
import com.dailyshopper.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    public final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Item Order Error", e.getMessage()));
        }
    }

    @PostMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Order Not Found", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrder(@PathVariable Long userId){
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("User Order Success!", orders));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("User Not Found", e.getMessage()));
        }

    }

}
