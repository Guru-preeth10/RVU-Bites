package com.example.rvubites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rvubites.databinding.ItemOrderBinding;
import com.example.rvubites.models.CartItem;
import com.example.rvubites.models.Order;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onTrackClick(Order order);
    }

    public OrdersAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.binding.tvOrderId.setText("Order #" + order.getOrderId().substring(Math.max(0, order.getOrderId().length() - 5)));
        holder.binding.tvDate.setText(order.getDate());
        holder.binding.tvStatus.setText(order.getStatus().toUpperCase());
        holder.binding.tvTotal.setText("Total: ₹" + String.format("%.2f", order.getTotalAmount()));

        StringBuilder itemsStr = new StringBuilder();
        if (order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                CartItem item = order.getItems().get(i);
                itemsStr.append(item.getQuantity()).append("x ").append(item.getFood().getName());
                if (i < order.getItems().size() - 1) itemsStr.append(", ");
            }
        }
        holder.binding.tvItems.setText(itemsStr.toString());

        holder.binding.btnTrack.setOnClickListener(v -> listener.onTrackClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}