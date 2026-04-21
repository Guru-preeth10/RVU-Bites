package com.example.rvubites.viewmodels;

import com.example.rvubites.models.Food;
import com.example.rvubites.repositories.FoodRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private final FoodRepository repository;
    private final LiveData<List<Food>> foods;

    public HomeViewModel() {
        repository = new FoodRepository();
        foods = repository.getFoods();
    }

    public LiveData<List<Food>> getFoods() {
        return foods;
    }
}