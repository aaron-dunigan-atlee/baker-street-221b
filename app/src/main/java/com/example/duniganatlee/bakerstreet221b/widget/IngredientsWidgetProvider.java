package com.example.duniganatlee.bakerstreet221b.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.mainscreen.MainActivity;
import com.example.duniganatlee.bakerstreet221b.recipescreen.RecipeListActivity;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.PreferenceUtils;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static String mRecipeListJson;
    private static int mCurrentRecipeId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        // Set the adapter for the ListView
        Intent intent = new Intent(context, IngredientsListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_ingredients, intent);
        // Handle case when no ingredients found
        views.setEmptyView(R.id.widget_list_ingredients, R.id.widget_text_no_recipe);

        // Find out if user has already selected a recipe.  If so, clicking will open that recipe's
        // RecipeListActivity.
        // If not, clicking the widget will open the MainActivity.
        mRecipeListJson = PreferenceUtils.getPreferenceRecipeJson(context);
        mCurrentRecipeId = PreferenceUtils.getPreferenceCurrentRecipeId(context);
        Log.d("Current recipe",Integer.toString(mCurrentRecipeId));

        // Pending intent to open the app's RecipeListActivity for the given recipe.
        // In this case, no need to differentiate which ingredient was clicked.
        Intent onClickIntent = new Intent(context, RecipeListActivity.class);
        //onClickIntent.putExtra(JsonUtils.RECIPE_JSON_EXTRA, mRecipeListJson);
        //onClickIntent.putExtra(JsonUtils.RECIPE_POSITION_EXTRA, mCurrentRecipeId);
        PendingIntent recipePendingIntent = PendingIntent.getActivity(context, 0, onClickIntent, 0);
        views.setPendingIntentTemplate(R.id.widget_list_ingredients, recipePendingIntent);

        // If no recipe is selected, open main activity when user clicks the EmptyView text view.
        Intent noRecipeIntent = new Intent(context, MainActivity.class);
        PendingIntent noRecipePendingIntent = PendingIntent.getActivity(context, 0, noRecipeIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_text_no_recipe, noRecipePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

