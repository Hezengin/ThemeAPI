package com.example.themeapi.view.detail;

import static com.example.themeapi.view.home.HomeActivity.EXTRA_DETAIL;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.example.themeapi.R;
import com.example.themeapi.Utils;
import com.example.themeapi.model.Meals;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity implements DetailView{

    Toolbar toolbar;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView mealThumb;
    TextView category;
    TextView country;
    TextView instructions;
    TextView ingredients;
    TextView measures;
    TextView youtube;
    TextView source;
    private Meals.Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mealThumb = findViewById(R.id.mealThumb);
        category = findViewById(R.id.category);
        country = findViewById(R.id.country);
        instructions = findViewById(R.id.instructions);
        ingredients = findViewById(R.id.ingredient);
        measures = findViewById(R.id.measure);
        youtube = findViewById(R.id.youtube);
        source = findViewById(R.id.source);

        setupActionBar();

        Intent intent = getIntent();
        String mealName = intent.getStringExtra(EXTRA_DETAIL);

        DetailPresenter presenter = new DetailPresenter(this);
        presenter.getMealById(mealName);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void setupColorActionBarIcon(Drawable favoriteItemColor) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);

            } else {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorWhite),
                        PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setMeal(Meals.Meal meal) {
        this.meal = meal;

        Picasso.get().load(meal.getStrMealThumb()).into(mealThumb);
        collapsingToolbarLayout.setTitle(meal.getStrMeal());
        category.setText(meal.getStrCategory());
        country.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());
        setupActionBar();

        List<String> ingredientsList = new ArrayList<>();
        List<String> measuresList = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            String ingredient = getIngredient(meal, i);
            String measure = getMeasure(meal, i);

            if (ingredient != null && !ingredient.isEmpty()) {
                ingredientsList.add("\n \u2022 " + ingredient);
            }

            if (measure != null && !measure.isEmpty() && !Character.isWhitespace(measure.charAt(0))) {
                measuresList.add("\n : " + measure);
            }
        }

        ingredients.setText(TextUtils.join("", ingredientsList));
        measures.setText(TextUtils.join("", measuresList));

        youtube.setOnClickListener(v -> {
            Intent intentYoutube = new Intent(Intent.ACTION_VIEW);
            intentYoutube.setData(Uri.parse(meal.getStrYoutube()));
            startActivity(intentYoutube);
        });

        source.setOnClickListener(v -> {
            Intent intentSource = new Intent(Intent.ACTION_VIEW);
            intentSource.setData(Uri.parse(meal.getStrSource()));
            startActivity(intentSource);
        });
    }

    private String getIngredient(Meals.Meal meal, int index) {
        String ingredient = null;
        try {
            Field field = meal.getClass().getDeclaredField("strIngredient" + index);
            ingredient = (String) field.get(meal);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ingredient;
    }

    private String getMeasure(Meals.Meal meal, int index) {
        String measure = null;
        try {
            Field field = meal.getClass().getDeclaredField("strMeasure" + index);
            measure = (String) field.get(meal);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return measure;
    }


    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this,"Error", message);
    }
}