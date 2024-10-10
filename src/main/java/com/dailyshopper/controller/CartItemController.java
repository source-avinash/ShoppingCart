package com.dailyshopper.controller;


import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Cart;
import com.dailyshopper.model.User;
import com.dailyshopper.response.ApiResponse;
import com.dailyshopper.service.cart.CartItemService;
import com.dailyshopper.service.cart.CartService;
import com.dailyshopper.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Controller
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final IUserService userService;


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse>  addItemToCart(
                                                      @RequestParam Long productId,
                                                      @RequestParam Integer quantity){

        try {
                User user = userService.getUserById(1L);
                Cart cart = cartService.initializeNewCart(user);


            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body( new ApiResponse("error", e.getMessage()));
        }

    }


    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body( new ApiResponse("error", e.getMessage()));
        }

    }


    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body( new ApiResponse("error", e.getMessage()));
        }
    }
}

