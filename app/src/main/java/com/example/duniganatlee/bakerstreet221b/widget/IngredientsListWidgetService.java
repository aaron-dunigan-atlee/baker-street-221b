package com.example.duniganatlee.bakerstreet221b.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Ingredient;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.PreferenceUtils;

import java.util.List;

public class IngredientsListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(this.getApplicationContext());
    }
}

class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    List<Ingredient> mIngredients;
    private String recipeListJson;
    private int currentRecipeId;

    public IngredientsListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }
    // Called on start and when notifyAppWidgetViewDataChanged is called.
    @Override
    public void onDataSetChanged() {
        recipeListJson = PreferenceUtils.getPreferenceRecipeJson(mContext);
        currentRecipeId = PreferenceUtils.getPreferenceCurrentRecipeId(mContext);
        if (recipeListJson != null && currentRecipeId != PreferenceUtils.NO_RECIPE_SELECTED) {
            Recipe recipe = JsonUtils.parseRecipeList(recipeListJson)[currentRecipeId];
            mIngredients = recipe.getIngredients();
        } else {
            mIngredients = null;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }


    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;
        Ingredient ingredient = mIngredients.get(position);
        String ingredientText = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_item);
        views.setTextViewText(R.id.widget_text_ingredient_item, ingredientText);

        // Set onclick intent using a fillInIntent which interprets the PendingIntentTemplate
        // which was set in IngredientsWidgetProvider's onCreate().
        // Since all ingredients have same click behavior, no need to fill in the intent template.
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(JsonUtils.RECIPE_POSITION_EXTRA, currentRecipeId);
        fillInIntent.putExtra(JsonUtils.RECIPE_JSON_EXTRA, recipeListJson);
        views.setOnClickFillInIntent(R.id.widget_text_ingredient_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
