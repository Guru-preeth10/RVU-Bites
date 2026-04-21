package com.example.rvubites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.rvubites.databinding.ItemCartBinding;
import com.example.rvubites.models.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemDeleted(int position);
    }

    public CartAdapter(List<CartItem> cartItems, OnCartItemChangeListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    public void updateList(List<CartItem> newList) {
        this.cartItems = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.binding.tvCartFoodName.setText(item.getFood().getName());
        holder.binding.tvCartFoodPrice.setText("₹" + String.format("%.2f", item.getFood().getPrice()));
        holder.binding.tvCartQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(holder.itemView.getContext())
                .load(item.getFood().getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(24))
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.binding.ivCartFood);

        holder.binding.btnCartPlus.setOnClickListener(v -> {
            listener.onQuantityChanged(holder.getAdapterPosition(), item.getQuantity() + 1);
        });

        holder.binding.btnCartMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                listener.onQuantityChanged(holder.getAdapterPosition(), item.getQuantity() - 1);
            } else {
                listener.onItemDeleted(holder.getAdapterPosition());
            }
        });

        holder.binding.btnDelete.setOnClickListener(v -> {
            listener.onItemDeleted(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}