package com.example.rvubites.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.rvubites.models.CartItem;
import com.example.rvubites.models.Food;
import com.example.rvubites.utils.CartManager;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartManager cartManager;
    private final MutableLiveData<Double> discountPercentage = new MutableLiveData<>(0.0);

    public CartViewModel() {
        this.cartManager = CartManager.getInstance();
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartManager.getCartItems();
    }

    public void updateQuantity(String foodId, int quantity) {
        cartManager.updateQuantity(foodId, quantity);
    }

    public void removeItem(int position) {
        List<CartItem> items = cartManager.getCartItems().getValue();
        if (items != null && position < items.size()) {
            cartManager.updateQuantity(items.get(position).getFood().getId(), 0);
        }
    }

    public void applyCoupon(String code) {
        if ("RVU30".equalsIgnoreCase(code)) {
            discountPercentage.setValue(0.30);
        } else {
            discountPercentage.setValue(0.0);
        }
    }

    public LiveData<Double> getDiscountPercentage() {
        return discountPercentage;
    }

    public double getItemTotal() {
        return cartManager.getItemTotal();
    }

    public void clearCart() {
        cartManager.clearCart();
    }
}