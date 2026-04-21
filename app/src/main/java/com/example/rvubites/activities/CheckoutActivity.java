package com.example.rvubites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rvubites.databinding.ActivityCheckoutBinding;
import com.example.rvubites.models.CartItem;
import com.example.rvubites.models.Order;
import com.example.rvubites.utils.CartManager;
import com.example.rvubites.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(this);
        binding.etAddress.setText(preferenceManager.getUserAddress());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnPlaceOrder.setOnClickListener(v -> {
            String address = binding.etAddress.getText().toString().trim();
            if (TextUtils.isEmpty(address)) {
                binding.etAddress.setError("Address is required");
                return;
            }

            preferenceManager.setUserAddress(address);
            placeOrder(address);
        });
    }

    private void placeOrder(String address) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "guest";
        List<CartItem> items = CartManager.getInstance().getCartItems().getValue();
        
        if (items == null || items.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = CartManager.getInstance().getItemTotal() + 20.0; // Adding delivery fee
        String orderId = String.valueOf(System.currentTimeMillis());
        String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

        Order order = new Order(orderId, date, total, "Pending", items);
        // Add userId to order if needed or create a separate document structure

        binding.btnPlaceOrder.setEnabled(false);
        db.collection("orders").document(orderId).set(order)
                .addOnSuccessListener(aVoid -> {
                    CartManager.getInstance().clearCart();
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    binding.btnPlaceOrder.setEnabled(true);
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}