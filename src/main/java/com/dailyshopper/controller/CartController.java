package com.dailyshopper.controller;


import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Cart;
import com.dailyshopper.response.ApiResponse;
import com.dailyshopper.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Controller
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final CartService cartService;


    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){

        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }
    }


    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){

        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }

    }


    @GetMapping("/{cartId}/total-amount")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){

        try {
            BigDecimal totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("success", totalAmount));
        } catch (Exception e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }


    }



}
