package com.example.rvubites.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.rvubites.databinding.ActivityFoodDetailsBinding;
import com.example.rvubites.models.Food;

public class FoodDetailsActivity extends AppCompatActivity {

    private ActivityFoodDetailsBinding binding;
    private Food food;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        food = (Food) getIntent().getSerializableExtra("food");
        if (food != null) {
            displayFoodDetails();
        }

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityUI();
        });

        binding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityUI();
            }
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(this, "Added " + quantity + " " + food.getName() + " to cart", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void displayFoodDetails() {
        binding.tvFoodName.setText(food.getName());
        binding.tvDescription.setText(food.getDescription());
        binding.tvRating.setText(food.getRating() + " (120+ reviews)");
        updateQuantityUI();

        Glide.with(this)
                .load(food.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivFoodLarge);
    }

    private void updateQuantityUI() {
        binding.tvQuantity.setText(String.valueOf(quantity));
        binding.tvPrice.setText("₹" + (int) (food.getPrice() * quantity));
        binding.btnAddToCart.setText("Add to Cart - ₹" + (int) (food.getPrice() * quantity));
    }
}