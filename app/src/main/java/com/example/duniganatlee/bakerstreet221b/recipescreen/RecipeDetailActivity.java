package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

import butterknife.BindView;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null) {
            // Extract intent extras.
            Intent sendingIntent = getIntent();
            String recipeJson = sendingIntent.getStringExtra(JsonUtils.RECIPE_JSON_EXTRA);
            int recipePosition = sendingIntent.getIntExtra(JsonUtils.RECIPE_POSITION_EXTRA, JsonUtils.POSITION_DEFAULT);
            int stepPosition = sendingIntent.getIntExtra(JsonUtils.STEP_POSITION_EXTRA, JsonUtils.POSITION_DEFAULT);
            Recipe[] recipes = JsonUtils.parseRecipeList(recipeJson);
            Recipe recipe = recipes[recipePosition];
            Step recipeStep = recipe.getSteps().get(stepPosition);
            setTitle(recipe.getName());
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            // TODO: Pass the details of the recipe step to the fragment.
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setRecipeStep(recipeStep);
            fragment.setRecipeName(recipe.getName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
