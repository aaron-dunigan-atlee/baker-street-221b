package com.example.duniganatlee.bakerstreet221b;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String mRecipeListJson = null;
    // private Toast noNetworkToast = Toast.makeText(this, "No network", Toast.LENGTH_SHORT);
    // private Toast noRecipesToast = Toast.makeText(this, "Cannot get recipes", Toast.LENGTH_SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            Recipe[] recipes = JsonUtils.parseRecipeList(mRecipeListJson);

        } else {
            //noRecipesToast.show();
            Toast.makeText(this, "Could not load recipes", Toast.LENGTH_SHORT).show();
        }
    }
}
