package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.example.duniganatlee.bakerstreet221b.recipescreen.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link RecipeStepMasterListFragment.OnStepClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecipeStepRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepRecyclerViewAdapter.ViewHolder> {

    private final List<Step> mRecipeSteps;
    private final RecipeStepMasterListFragment.OnStepClickListener mListener;

    public RecipeStepRecyclerViewAdapter(Recipe recipe, RecipeStepMasterListFragment.OnStepClickListener listener) {
        mRecipeSteps = recipe.getSteps();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStep = mRecipeSteps.get(position);
        holder.mIdView.setText(Integer.toString(mRecipeSteps.get(position).getId()));
        holder.mContentView.setText(mRecipeSteps.get(position).getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onStepSelected(holder.mStep);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Step mStep;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.step_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
