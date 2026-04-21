package com.example.rvubites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.rvubites.adapters.OrdersAdapter;
import com.example.rvubites.databinding.ActivityOrdersBinding;
import com.example.rvubites.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ActivityOrdersBinding binding;
    private OrdersAdapter adapter;
    private List<Order> orderList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        setupRecyclerView();
        fetchOrders();
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(orderList, order -> {
            Intent intent = new Intent(OrdersActivity.this, TrackOrderActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrders.setAdapter(adapter);
    }

    private void fetchOrders() {
        if (mAuth.getCurrentUser() == null) return;

        // Note: For simplicity, we fetch all orders. In a real app, filter by userId.
        db.collection("orders")
                .orderBy("orderId", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            orderList.add(document.toObject(Order.class));
                        }
                        adapter.notifyDataSetChanged();
                        updateEmptyState();
                    } else {
                        Toast.makeText(this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmptyState() {
        if (orderList.isEmpty()) {
            binding.emptyState.setVisibility(View.VISIBLE);
            binding.rvOrders.setVisibility(View.GONE);
        } else {
            binding.emptyState.setVisibility(View.GONE);
            binding.rvOrders.setVisibility(View.VISIBLE);
        }
    }
}