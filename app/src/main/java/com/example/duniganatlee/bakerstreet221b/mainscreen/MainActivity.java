package com.example.duniganatlee.bakerstreet221b.mainscreen;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements RecipeCardAdapter.OnClickHandler {
    private String mRecipeListJson = null;
    public Recipe[] mRecipes = new Recipe[0];
    // private Toast noNetworkToast = Toast.makeText(this, "No network", Toast.LENGTH_SHORT);
    // private Toast noRecipesToast = Toast.makeText(this, "Cannot get recipes", Toast.LENGTH_SHORT);
    private RecyclerView.LayoutManager cardLayoutManager;
    private RecipeCardAdapter mRecipeCardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up Recycler View
        RecyclerView recipeCardRecyclerView = (RecyclerView) findViewById(R.id.recipe_cards_rv);
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
            //noNetworkToast.show();
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this,Integer.toString(position),Toast.LENGTH_SHORT).show();
        Log.d("Position clicked",Integer.toString(position));
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
        }
    }

    private void populateRecipeCards() {
        if (mRecipeListJson != null) {
            mRecipes = JsonUtils.parseRecipeList(mRecipeListJson);
            mRecipeCardAdapter.setRecipeTitles(mRecipes);
        } else {
            //noRecipesToast.show();
            Toast.makeText(this, "Could not load recipes", Toast.LENGTH_SHORT).show();
        }
    }
}
