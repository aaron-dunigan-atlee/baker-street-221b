package com.example.duniganatlee.bakerstreet221b.mainscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Recipe;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {
    // TODO: Make itemCount dynamic.
    private static int itemCount = 4;
    private OnClickHandler recipeCardClickHandler;
    private String[] recipeTitles = new String[itemCount];
    public Context adapterContext;
    public RecipeCardAdapter(Context context, OnClickHandler clickHandler) {
        adapterContext = context;
        recipeCardClickHandler = clickHandler;
    }
    class RecipeCardViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        public final TextView titleTextView;
        public RecipeCardViewHolder(View rootView) {
            super(rootView);
            titleTextView = (TextView) rootView.findViewById(R.id.recipe_card_title_tv);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            recipeCardClickHandler.onItemClicked(adapterPosition);
        }
    }

    @NonNull
    @Override
    public RecipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForRecipeCard = R.layout.recipe_card;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForRecipeCard, parent, false);
        RecipeCardViewHolder viewHolder = new RecipeCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardViewHolder holder, int position) {
        holder.titleTextView.setText(recipeTitles[position]);

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public interface OnClickHandler {
        void onItemClicked(int position);
    }

    public void setRecipeTitles(Recipe[] recipes) {
        setItemCount(recipes.length);
        recipeTitles = new String[itemCount];
        for (int i=0; i<itemCount; i++) {
            recipeTitles[i] = recipes[i].getName();
        }
        notifyDataSetChanged();
    }

    public static void setItemCount(int itemCount) {
        RecipeCardAdapter.itemCount = itemCount;
    }
}
