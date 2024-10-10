package com.dailyshopper.service.cart;

import com.dailyshopper.model.Cart;
import com.dailyshopper.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long user);

}
