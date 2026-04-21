package com.example.rvubites.repositories;

import com.example.rvubites.models.Food;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FoodRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<Food>> getFoods() {
        MutableLiveData<List<Food>> foodsLiveData = new MutableLiveData<>();
        
        db.collection("foods").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                List<Food> foods = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    foods.add(document.toObject(Food.class));
                }
                foodsLiveData.setValue(foods);
            } else {
                // Fallback: If Firestore is empty, return 10 Popular items
                foodsLiveData.setValue(getPopularFoods());
            }
        });
        return foodsLiveData;
    }

    private List<Food> getPopularFoods() {
        List<Food> popularFoods = new ArrayList<>();
        popularFoods.add(new Food("1", "Premium Masala Dosa", "Crispy golden dosa with spicy potato mash", 55.0, "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500", "Snacks", 4.8f));
        popularFoods.add(new Food("2", "Classic Veg Burger", "Premium patty with cheese and fresh veggies", 85.0, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Snacks", 4.4f));
        popularFoods.add(new Food("3", "Iced Cold Coffee", "Rich creamy blend of premium coffee", 65.0, "https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=500", "Drinks", 4.9f));
        popularFoods.add(new Food("4", "Spicy Paneer Roll", "Soft paneer cubes in spicy tandoori sauce", 75.0, "https://images.unsplash.com/photo-1626776876729-bab4369a5a54?w=500", "Snacks", 4.5f));
        popularFoods.add(new Food("5", "North Indian Thali", "Rice, dal, two curries and fresh butter roti", 140.0, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Meals", 4.7f));
        popularFoods.add(new Food("6", "Cheese Margherita", "Loaded with mozzarella and fresh toppings", 190.0, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Snacks", 4.3f));
        popularFoods.add(new Food("7", "Butter Chicken", "Creamy tomato gravy with tender chicken pieces", 220.0, "https://images.rawpixel.com/image_800/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIyLTA1L2stYTM5LWV4LXBpeGFiYXktNDY3MTU3LmpwZw.jpg", "Meals", 4.9f));
        popularFoods.add(new Food("8", "Hakka Noodles", "Wok-tossed noodles with fresh seasonal veggies", 110.0, "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=500", "Meals", 4.2f));
        popularFoods.add(new Food("9", "Chocolate Brownie", "Warm fudgy brownie with chocolate drizzle", 80.0, "https://images.unsplash.com/photo-1564355808539-22fda35bed7e?w=500", "Desserts", 4.8f));
        popularFoods.add(new Food("10", "Fresh Lime Soda", "Refreshing sweet and salty lemon fizz", 45.0, "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500", "Drinks", 4.1f));
        return popularFoods;
    }
}