package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.example.duniganatlee.bakerstreet221b.recipescreen.dummy.DummyContent;

/**
 * A fragment representing a list of Recipe Step short descriptions.
 * Activities containing this fragment MUST implement the {@link OnStepClickListener}
 * interface.
 */
public class RecipeStepMasterListFragment extends Fragment {
    private OnStepClickListener mListener;
    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepMasterListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        // Get the steps for the recipe.
        // savedInstanceState.getString()
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecipeStepRecyclerViewAdapter(mRecipe, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface allows this fragment to communicate back to the container activity.
     * The container activity must implement the interface.
     **/
    public interface OnStepClickListener {
        void onStepSelected(Step recipeStep);
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }
}
