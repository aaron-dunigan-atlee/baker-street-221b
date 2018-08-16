package com.example.duniganatlee.bakerstreet221b.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* We are using SharedPreferences for local storage of the entire recipe JSON and
 * the id/position of the current recipe, primarily so the widget and the app can both
 * access this data.  These methods set and get these preferences.
 */
public class PreferenceUtils {
    private static final String RECIPE_JSON_KEY = "recipe_json_key";
    private static final String RECIPE_POSITION_KEY = "recipe_position_key";
    public static final int NO_RECIPE_SELECTED = -1;

    public static void setPreferenceRecipeJson(Context context, String json) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.putString(RECIPE_JSON_KEY, json);
        preferenceEditor.apply();
    }

    public static void setPreferenceCurrentRecipeId(Context context, int recipePosition) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.putInt(RECIPE_POSITION_KEY, recipePosition);
        preferenceEditor.apply();
    }

    public static String getPreferenceRecipeJson(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(RECIPE_JSON_KEY, null);
    }

    public static int getPreferenceCurrentRecipeId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(RECIPE_POSITION_KEY, NO_RECIPE_SELECTED);
    }
}
