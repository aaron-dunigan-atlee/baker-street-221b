package com.example.duniganatlee.bakerstreet221b.utils;

import android.util.Log;

import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.google.gson.Gson;

import java.util.List;

public class JsonUtils {
    // Constants for identifying intent extras.
    public static final String RECIPE_POSITION_EXTRA = "recipe_id_extra";
    public static final String RECIPE_JSON_EXTRA = "recipe_json_extra";
    public static final String STEP_POSITION_EXTRA = "step_id_extra";
    public final static int POSITION_DEFAULT = -1;

    /*
    Given the JSON recipe list, extract each individual recipe, assign it to a Recipe object,
    and return an array of all Recipes.
     */
    public static Recipe[] parseRecipeList(String jsonRecipeList) {
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(jsonRecipeList, Recipe[].class);
        Log.d("Recipes found",Integer.toString(recipes.length));
        return recipes;
    }
}
