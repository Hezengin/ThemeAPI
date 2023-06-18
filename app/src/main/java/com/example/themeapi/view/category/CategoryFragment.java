package com.example.themeapi.view.category;

import static com.example.themeapi.view.home.HomeActivity.EXTRA_DETAIL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.themeapi.R;
import com.example.themeapi.Utils;
import com.example.themeapi.adapters.RecyclerViewMealByCategory;
import com.example.themeapi.model.Meals;
import com.example.themeapi.view.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryView {

    private RecyclerView recyclerView;
    private ImageView imageCategory;
    private TextView textCategory;
    private AlertDialog.Builder descDialog;
    private CardView cardCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        imageCategory = view.findViewById(R.id.imageCategory);
        textCategory = view.findViewById(R.id.textCategory);
        cardCategory = view.findViewById(R.id.cardCategory);

        cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descDialog.setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss());
                descDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            textCategory.setText(getArguments().getString("EXTRA_DATA_DESC"));
            Picasso.get()
                    .load(getArguments().getString("EXTRA_DATA_IMAGE"))
                    .into(imageCategory);
            descDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getArguments().getString("EXTRA_DATA_NAME"))
                    .setMessage(getArguments().getString("EXTRA_DATA_DESC"));

            CategoryPresenter presenter = new CategoryPresenter(this);
            presenter.getMealByCategory(getArguments().getString("EXTRA_DATA_NAME"));
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setMeals(List<Meals.Meal> meals) {
        RecyclerViewMealByCategory adapter = new RecyclerViewMealByCategory(getActivity(), meals);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position) -> {
            TextView mealName = view.findViewById(R.id.mealName);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL,mealName.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(getActivity(), "Error ", message);
    }
}