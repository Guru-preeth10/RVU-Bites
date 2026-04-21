package com.example.rvubites.utils;

import com.example.rvubites.models.CartItem;
import com.example.rvubites.models.Food;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CartManager {
    private static CartManager instance;
    private final MutableLiveData<List<CartItem>> cartItems = new MutableLiveData<>(new ArrayList<>());

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public void addToCart(Food food) {
        List<CartItem> currentItems = cartItems.getValue();
        if (currentItems == null) currentItems = new ArrayList<>();

        boolean found = false;
        for (CartItem item : currentItems) {
            if (item.getFood().getId().equals(food.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            currentItems.add(new CartItem(food, 1));
        }

        cartItems.setValue(currentItems);
    }

    public void updateQuantity(String foodId, int quantity) {
        List<CartItem> currentItems = cartItems.getValue();
        if (currentItems == null) return;

        for (int i = 0; i < currentItems.size(); i++) {
            if (currentItems.get(i).getFood().getId().equals(foodId)) {
                if (quantity <= 0) {
                    currentItems.remove(i);
                } else {
                    currentItems.get(i).setQuantity(quantity);
                }
                break;
            }
        }
        cartItems.setValue(currentItems);
    }

    public void clearCart() {
        cartItems.setValue(new ArrayList<>());
    }
    
    public double getItemTotal() {
        double total = 0;
        List<CartItem> items = cartItems.getValue();
        if (items != null) {
            for (CartItem item : items) {
                total += item.getTotalPrice();
            }
        }
        return total;
    }
}