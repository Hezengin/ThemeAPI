package com.example.themeapi;

import android.app.AlertDialog;
import android.content.Context;

import com.example.themeapi.api.TheMealAPI;
import com.example.themeapi.api.TheMealClient;

public class Utils {

    public static TheMealAPI getApi() {
        return TheMealClient.getMealClient().create(TheMealAPI.class);
    }

    public static AlertDialog showDialogMessage(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        return alertDialog;
    }
}