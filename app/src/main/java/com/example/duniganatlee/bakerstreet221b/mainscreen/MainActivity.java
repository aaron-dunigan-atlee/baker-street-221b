package com.example.duniganatlee.bakerstreet221b.mainscreen;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.recipescreen.RecipeListActivity;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.NetworkUtils;
import com.example.duniganatlee.bakerstreet221b.utils.PreferenceUtils;
import com.example.duniganatlee.bakerstreet221b.widget.IngredientsWidgetProvider;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements RecipeCardAdapter.OnClickHandler {
    // Bind views with ButterKnife.
    @BindView(R.id.recipe_cards_rv) RecyclerView recipeCardRecyclerView;
    private String mRecipeListJson = null;
    public Recipe[] mRecipes = new Recipe[0];
    private RecyclerView.LayoutManager cardLayoutManager;
    private RecipeCardAdapter mRecipeCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Package name", getPackageName());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cardLayoutManager = new LinearLayoutManager(this);
        recipeCardRecyclerView.setLayoutManager(cardLayoutManager);
        // Set adapter for the RecyclerView.
        mRecipeCardAdapter = new RecipeCardAdapter(this, this);
        recipeCardRecyclerView.setAdapter(mRecipeCardAdapter);
        // Populate UI
        fetchRecipesAndPopulateCards();
    }
    /*
    Fetch recipes json from URL using an AsyncTask, and use result to populate
    the card views.
     */
    private void fetchRecipesAndPopulateCards() {
        if (NetworkUtils.deviceIsConnected(this)) {
            String recipeUrlString = getString(R.string.recipe_url);
            URL recipeUrl = NetworkUtils.buildUrl(recipeUrlString);
            RecipeQueryTask queryTask = new RecipeQueryTask();
            queryTask.execute(recipeUrl);
        } else {

            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(int position) {
        PreferenceUtils.setPreferenceCurrentRecipeId(this, position);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_ingredients);
        Intent launchRecipeListIntent = new Intent(this, RecipeListActivity.class);
        launchRecipeListIntent.putExtra(JsonUtils.RECIPE_JSON_EXTRA, mRecipeListJson);
        launchRecipeListIntent.putExtra(JsonUtils.RECIPE_POSITION_EXTRA, position);
        startActivity(launchRecipeListIntent);
    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String queryResult) {
            super.onPostExecute(queryResult);
            mRecipeListJson = queryResult;
            populateRecipeCards();
            PreferenceUtils.setPreferenceRecipeJson(getApplicationContext(),queryResult);

        }
    }

    private void populateRecipeCards() {
        if (mRecipeListJson != null) {
            mRecipes = JsonUtils.parseRecipeList(mRecipeListJson);
            mRecipeCardAdapter.setRecipeTitles(mRecipes);
        } else {

            Toast.makeText(this, "Could not load recipes", Toast.LENGTH_SHORT).show();
        }
    }
}
