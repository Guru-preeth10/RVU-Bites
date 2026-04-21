package com.example.rvubites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.rvubites.adapters.CartAdapter;
import com.example.rvubites.databinding.ActivityCartBinding;
import com.example.rvubites.models.CartItem;
import com.example.rvubites.viewmodels.CartViewModel;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private CartViewModel viewModel;
    
    private static final double DELIVERY_FEE = 20.0;
    private static final double GST_RATE = 0.05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        setupCartList();
        observeViewModel();
        
        binding.btnApplyCoupon.setOnClickListener(v -> {
            String coupon = binding.etCoupon.getText().toString().trim();
            viewModel.applyCoupon(coupon);
        });

        binding.btnCheckout.setOnClickListener(v -> {
            List<CartItem> items = viewModel.getCartItems().getValue();
            if (items == null || items.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });

        // "Browse Food" navigation fix
        binding.layoutEmptyCart.findViewById(com.example.rvubites.R.id.btnBrowseFood).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setupCartList() {
        cartAdapter = new CartAdapter(new ArrayList<>(), new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(int position, int newQuantity) {
                List<CartItem> items = viewModel.getCartItems().getValue();
                if (items != null && position < items.size()) {
                    viewModel.updateQuantity(items.get(position).getFood().getId(), newQuantity);
                }
            }

            @Override
            public void onItemDeleted(int position) {
                viewModel.removeItem(position);
            }
        });

        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void observeViewModel() {
        viewModel.getCartItems().observe(this, items -> {
            if (items == null || items.isEmpty()) {
                binding.layoutEmptyCart.setVisibility(View.VISIBLE);
                binding.scrollViewCart.setVisibility(View.GONE);
                binding.bottomBar.setVisibility(View.GONE);
            } else {
                binding.layoutEmptyCart.setVisibility(View.GONE);
                binding.scrollViewCart.setVisibility(View.VISIBLE);
                binding.bottomBar.setVisibility(View.VISIBLE);
                cartAdapter.updateList(items);
                calculateBill(items, viewModel.getDiscountPercentage().getValue());
            }
        });

        viewModel.getDiscountPercentage().observe(this, discount -> {
            List<CartItem> items = viewModel.getCartItems().getValue();
            if (items != null) {
                calculateBill(items, discount);
            }
            if (discount > 0) {
                Toast.makeText(this, "Coupon Applied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateBill(List<CartItem> items, Double discountPercentage) {
        double itemTotal = 0;
        for (CartItem item : items) {
            itemTotal += item.getFood().getPrice() * item.getQuantity();
        }

        double discountAmount = itemTotal * (discountPercentage != null ? discountPercentage : 0);
        double subtotalAfterDiscount = itemTotal - discountAmount;
        double gst = subtotalAfterDiscount * GST_RATE;
        double totalToPay = subtotalAfterDiscount + DELIVERY_FEE + gst;

        binding.tvItemTotal.setText("₹" + String.format("%.2f", itemTotal));
        
        if (discountAmount > 0) {
            binding.layoutDiscount.setVisibility(View.VISIBLE);
            binding.tvDiscountAmount.setText("-₹" + String.format("%.2f", discountAmount));
        } else {
            binding.layoutDiscount.setVisibility(View.GONE);
        }
        
        binding.tvGST.setText("₹" + String.format("%.2f", gst));
        binding.tvGrandTotal.setText("₹" + String.format("%.2f", totalToPay));
        binding.tvBottomPrice.setText("₹" + String.format("%.2f", totalToPay));
    }
}