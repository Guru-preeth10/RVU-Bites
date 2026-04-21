package com.example.rvubites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.rvubites.databinding.ItemFoodBinding;
import com.example.rvubites.models.Food;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foodList;
    private OnFoodClickListener listener;

    public interface OnFoodClickListener {
        void onFoodClick(Food food);
        void onAddToCart(Food food);
    }

    public FoodAdapter(List<Food> foodList, OnFoodClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    public void updateList(List<Food> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.binding.tvFoodName.setText(food.getName());
        holder.binding.tvFoodDescription.setText(food.getDescription());
        holder.binding.tvPrice.setText("₹" + (int)food.getPrice());
        holder.binding.tvRating.setText(String.valueOf(food.getRating()));

        Glide.with(holder.itemView.getContext())
                .load(food.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.binding.ivFood);

        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
        holder.binding.btnAdd.setOnClickListener(v -> listener.onAddToCart(food));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ItemFoodBinding binding;
        FoodViewHolder(ItemFoodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}