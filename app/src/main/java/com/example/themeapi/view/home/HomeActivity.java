package com.example.themeapi.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.themeapi.R;
import com.example.themeapi.Utils;
import com.example.themeapi.adapters.RecyclerViewHomeAdapter;
import com.example.themeapi.model.Categories;
import com.example.themeapi.model.Meals;
import com.example.themeapi.view.category.CategoryActivity;
import com.example.themeapi.view.detail.DetailActivity;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements HomeView {

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    private RecyclerView recyclerViewCategory;
    private Context context;
    private EditText editText;
    private Button button;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewCategory = findViewById(R.id.recyclerCategory);
        editText = findViewById(R.id.search_bar);
        button = findViewById(R.id.search_button);
        context = getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            String mealName = editText.getText().toString();
            Utils.getApi().getMealByName(mealName).enqueue(new Callback<Meals>() {
                @Override
                public void onResponse(Call<Meals> call, Response<Meals> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Meals.Meal> meals = response.body().getMeals();
                        if (meals != null && !meals.isEmpty()) {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(EXTRA_DETAIL, mealName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Meal not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Meals> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

        presenter = new HomePresenter(this);
        presenter.getCategories();
    }

    @Override
    public void showLoading() {
        findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
    }
    @Override
    public void hideLoading() {findViewById(R.id.shimmerCategory).setVisibility(View.GONE);}

    @Override
    public void setCategory(List<Categories.Category> category) {
        RecyclerViewHomeAdapter homeAdapter = new RecyclerViewHomeAdapter(category, this);
        recyclerViewCategory.setAdapter(homeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        homeAdapter.notifyDataSetChanged();

        homeAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY, (Serializable) category);
            intent.putExtra(EXTRA_POSITION, position);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Title", message);
    }

}