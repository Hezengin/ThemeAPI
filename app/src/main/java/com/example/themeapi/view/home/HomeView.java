package com.example.themeapi.view.home;

import com.example.themeapi.model.Categories;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);

}
