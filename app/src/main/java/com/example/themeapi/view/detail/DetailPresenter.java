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

        view.showLoading();
        Utils.getApi().getMealByName(mealName).enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.setMeal(response.body().getMeals().get(0));
                } else{
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
        //TODO #5 Call the void show loading before starting to make a request to the server

        //TODO #6 Make a request to the server (Don't forget to hide loading when the response is received)

        //TODO #7 Set response (meal)
    }
}