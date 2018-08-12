package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.duniganatlee.bakerstreet221b.R;

import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
// TODO: Add ingredients to the UI.
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        if (savedInstanceState == null) {
            // If there is no previously saved state, get intent extras to parse the desired recipe,
            // and create a master list fragment.
            Intent intent = getIntent();
            mRecipeListJson = intent.getStringExtra(JsonUtils.RECIPE_JSON_EXTRA);
            mRecipePosition = intent.getIntExtra(JsonUtils.RECIPE_POSITION_EXTRA, JsonUtils.POSITION_DEFAULT);
        } else {
            mRecipeListJson = savedInstanceState.getString(RECIPE_JSON_KEY);
            mRecipePosition = savedInstanceState.getInt(RECIPE_POSITION_KEY);
        }
        if (mRecipeListJson == null) {
            Toast.makeText(this, "Could not load recipe.", Toast.LENGTH_LONG);
            finish();
        }
        // Parse recipe json.
        mRecipes = JsonUtils.parseRecipeList(mRecipeListJson);
        mRecipe = mRecipes[mRecipePosition];
        setTitle(mRecipe.getName());

        // Create fragment.
        RecipeStepMasterListFragment masterFragment = new RecipeStepMasterListFragment();
        masterFragment.setRecipe(mRecipe);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container, masterFragment)
                .commit();




        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeStepRecyclerViewAdapter(mRecipe, this));
    }

    @Override
    public void onStepSelected(Step recipeStep) {
        if (mTwoPane) {

        } else {
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
