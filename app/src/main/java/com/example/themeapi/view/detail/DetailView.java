package com.example.themeapi.view.detail;

import com.example.themeapi.model.Meals;

public interface DetailView {
    void setMeal(Meals.Meal meals);
    void onErrorLoading(String message);
}
