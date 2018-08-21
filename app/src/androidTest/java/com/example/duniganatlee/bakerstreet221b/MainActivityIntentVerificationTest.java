package com.example.duniganatlee.bakerstreet221b;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.duniganatlee.bakerstreet221b.mainscreen.MainActivity;
import com.example.duniganatlee.bakerstreet221b.utils.JsonUtils;
import com.example.duniganatlee.bakerstreet221b.utils.PreferenceUtils;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/* A test to verify the intent when clicking a button from the Main Activity.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentVerificationTest {
    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    // Test on RecyclerView based on examples at https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
    // Test that all four recipe cards generate intents with the proper extras.
    @Test
    public void clickButton_Initiates_RecipeListActivity() {
        for (int i = 0;i<4; i++) {
            Log.d("position", Integer.toString(i));

            // Scroll to position in RecyclerView list and click on it.
            onView(ViewMatchers.withId(R.id.recipe_cards_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            // Verify that extras were included.
            intended(allOf(
                    hasExtraWithKey(JsonUtils.RECIPE_JSON_EXTRA),
                    hasExtra(JsonUtils.RECIPE_POSITION_EXTRA, i)));

            // Verify that the recipe id stored in SharedPreferences is the same as the one
            // passed as the extra.
            Context context = getTargetContext();
            int storedPositionInSavedPreferences = PreferenceUtils.getPreferenceCurrentRecipeId(context);
            assert storedPositionInSavedPreferences == i;

            // Go back to main screen to repeat the test for the next position.
            pressBack();
        }
        }



}
