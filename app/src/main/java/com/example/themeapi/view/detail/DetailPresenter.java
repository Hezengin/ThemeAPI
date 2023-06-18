package com.example.themeapi.view.detail;

import com.example.themeapi.Utils;
import com.example.themeapi.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter {
    private DetailView view;

    public DetailPresenter(DetailView view) {
        this.view = view;
    }

    public void getMealById(String mealName) {

        Utils.getApi().getMealByName(mealName).enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                if (response.isSuccessful() && response.body() != null){
                    view.setMeal(response.body().getMeals().get(0));
                } else{
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}