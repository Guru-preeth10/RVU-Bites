package com.example.rvubites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rvubites.databinding.ItemOrderBinding;
import com.example.rvubites.models.Order;
import com.example.rvubites.models.CartItem;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onTrackClick(Order order);
    }

    public OrderAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
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
        Order order = orderList.get(position);
        holder.binding.tvOrderId.setText("Order #" + order.getOrderId());
        holder.binding.tvDate.setText(order.getDate());
        holder.binding.tvStatus.setText(order.getStatus());
        holder.binding.tvTotal.setText("Total: ₹" + String.format("%.2f", order.getTotalAmount()));
        
        // Join item names for display
        StringBuilder itemsText = new StringBuilder();
        if (order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                CartItem item = order.getItems().get(i);
                itemsText.append(item.getQuantity())
                        .append("x ")
                        .append(item.getFood().getName());
                if (i < order.getItems().size() - 1) itemsText.append(", ");
            }
        }
        holder.binding.tvItems.setText(itemsText.toString());

        holder.binding.btnTrack.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrackClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
