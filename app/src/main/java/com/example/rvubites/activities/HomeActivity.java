package com.example.rvubites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.rvubites.R;
import com.example.rvubites.adapters.CategoryAdapter;
import com.example.rvubites.adapters.FoodAdapter;
import com.example.rvubites.databinding.ActivityHomeBinding;
import com.example.rvubites.models.Food;
import com.example.rvubites.utils.CartManager;
import com.example.rvubites.utils.PreferenceManager;
import com.example.rvubites.viewmodels.HomeViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private PreferenceManager preferenceManager;
    private FoodAdapter foodAdapter;
    private CategoryAdapter categoryAdapter;
    private HomeViewModel viewModel;
    private List<Food> allFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        preferenceManager = new PreferenceManager(this);
        
        binding.tvGreeting.setText("Hello, " + preferenceManager.getUserName());

        setupCategories();
        setupBottomNav();
        setupSearch();
        
        binding.shimmerView.startShimmer();
        viewModel.getFoods().observe(this, foods -> {
            binding.shimmerView.stopShimmer();
            binding.shimmerView.setVisibility(View.GONE);
            if (foods != null && !foods.isEmpty()) {
                this.allFoods = foods;
                setupFoodList(foods);
                binding.rvPopularFood.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFoods(query, null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFoods(newText, null);
                return true;
            }
        });
    }

    private void setupCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Snacks");
        categories.add("Meals");
        categories.add("Drinks");
        categories.add("Desserts");

        categoryAdapter = new CategoryAdapter(categories, category -> {
            filterFoods(null, category);
        });
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void filterFoods(String query, String category) {
        List<Food> filteredList = allFoods;

        if (category != null && !category.equals("All")) {
            filteredList = filteredList.stream()
                    .filter(f -> f.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        if (query != null && !query.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(f -> f.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        foodAdapter.updateList(filteredList);
    }

    private void setupFoodList(List<Food> foods) {
        foodAdapter = new FoodAdapter(foods, new FoodAdapter.OnFoodClickListener() {
            @Override
            public void onFoodClick(Food food) {
                Intent intent = new Intent(HomeActivity.this, FoodDetailsActivity.class);
                intent.putExtra("food", food);
                startActivity(intent);
            }

            @Override
            public void onAddToCart(Food food) {
                CartManager.getInstance().addToCart(food);
                Toast.makeText(HomeActivity.this, "✓ " + food.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvPopularFood.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvPopularFood.setAdapter(foodAdapter);
    }

    private void setupBottomNav() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            }
            if (id == R.id.nav_orders) {
                startActivity(new Intent(this, OrdersActivity.class));
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}