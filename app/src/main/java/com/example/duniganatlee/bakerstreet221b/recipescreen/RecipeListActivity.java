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
            if (mRecipeListJson == null) {
                Toast.makeText(this, "Could not load recipe.", Toast.LENGTH_LONG);
                finish();
            }
            // Parse recipe json.
            mRecipes = JsonUtils.parseRecipeList(mRecipeListJson);
            mRecipe = mRecipes[mRecipePosition];

            // Create fragment.
            RecipeStepMasterListFragment masterFragment = new RecipeStepMasterListFragment();
            masterFragment.setRecipe(mRecipe);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, masterFragment)
                    .commit();


        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        /*
        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
