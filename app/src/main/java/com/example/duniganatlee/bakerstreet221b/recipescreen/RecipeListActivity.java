package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.duniganatlee.bakerstreet221b.R;

import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.PreferenceUtils;

/**
 * An activity representing a list of Recipe Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipeStepMasterListFragment.OnStepClickListener {

    private String mRecipeListJson;
    private int mRecipePosition;
    private Recipe[] mRecipes = new Recipe[0];
    private Recipe mRecipe;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private final String RECIPE_JSON_KEY = "recipe_json_key";
    private final String RECIPE_POSITION_KEY = "recipe_position_key";
    private final static String LOG_TAG = "RecipeListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        if (savedInstanceState == null) {
            // If there is no previously saved state, get the whole json and the id for the
            // desired recipe from the preferences.
            mRecipeListJson = PreferenceUtils.getPreferenceRecipeJson(this);
            mRecipePosition = PreferenceUtils.getPreferenceCurrentRecipeId(this);
            Log.d(LOG_TAG, "Getting preferences.");
        } else {
            // Otherwise, get the info from the saved instance state.
            mRecipeListJson = savedInstanceState.getString(RECIPE_JSON_KEY);
            mRecipePosition = savedInstanceState.getInt(RECIPE_POSITION_KEY);
            Log.d(LOG_TAG, "Getting savedInstanceState.");
        }
        if (mRecipeListJson == null || mRecipePosition == PreferenceUtils.NO_RECIPE_SELECTED) {
            Log.d(LOG_TAG, "No recipe.  Failing...");
            Toast.makeText(this, "Could not load recipe.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Parse recipe json.
        mRecipes = JsonUtils.parseRecipeList(mRecipeListJson);
        mRecipe = mRecipes[mRecipePosition];
        setTitle(mRecipe.getName());

        // Create ingredients fragment.
        IngredientsListFragment ingredientsFragment = new IngredientsListFragment();
        ingredientsFragment.setRecipe(mRecipe);

        // Create recipe steps fragment.
        RecipeStepMasterListFragment masterFragment = new RecipeStepMasterListFragment();
        masterFragment.setRecipe(mRecipe);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container, masterFragment)
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            // Therefore, set up the recipe detail fragment if this is a new instance:
            if (savedInstanceState == null) {
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setRecipeStep(mRecipe.getSteps().get(0));
                recipeStepDetailFragment.setRecipeName(mRecipe.getName());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, recipeStepDetailFragment)
                        .commit();
            }
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeStepRecyclerViewAdapter(mRecipe, this));
    }

    @Override
    public void onStepSelected(Step recipeStep) {
        if (mTwoPane) {
            // We've got a wide screen, so replace the detail fragment:
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setRecipeStep(recipeStep);
            recipeStepDetailFragment.setRecipeName(mRecipe.getName());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, recipeStepDetailFragment)
                    .commit();
        } else {
            // We've got a narrow screen, so launch an intent:
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(JsonUtils.RECIPE_JSON_EXTRA,mRecipeListJson);
            intent.putExtra(JsonUtils.RECIPE_POSITION_EXTRA, mRecipePosition);
            intent.putExtra(JsonUtils.STEP_POSITION_EXTRA,recipeStep.getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_POSITION_KEY, mRecipePosition);
        outState.putString(RECIPE_JSON_KEY, mRecipeListJson);
        super.onSaveInstanceState(outState);
    }
}
