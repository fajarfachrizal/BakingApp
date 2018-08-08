package com.example.fajar.bakingapp.ui.adapter;


/**
 * Brownies Photo by Alisa Anton on Unsplash
 * Nutella Pie Photo by Charles Deluvio 🇵🇭🇨🇦 on Unsplash
 * Yellow Cake Photo by Alireza Etemadi on Unsplash
 * Cheesecake Photo by Natalia Y on Unsplash
 */


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fajar.bakingapp.R;
import com.example.fajar.bakingapp.database.RecipeColumns;
import com.example.fajar.bakingapp.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder> {

    public static final String RECIPE_ID = "recipeId";
    public static final String RECIPE_NAME = "recipeName";

    private final RecipeListAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;


    public RecipeListAdapter(RecipeListAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_recipe;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.NAME));


        holder.recipeName.setText(name);
        holder.recipeServings.setText(mCursor.getString(mCursor.getColumnIndex(RecipeColumns.SERVINGS)));

        String url = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.IMAGE));
        if (url.equals("") || url.length() <= 0)
            url = null;


        if (name.equals("Nutella Pie")) {
            GlideApp.with(holder.itemView)
                    .load(url)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.pie)
                    .fallback(R.drawable.pie)
                    .into(holder.recipeImage);
        } else if (name.equals("Brownies")) {
            GlideApp.with(holder.itemView)
                    .load(url)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.brownies)
                    .fallback(R.drawable.brownies)
                    .into(holder.recipeImage);

        } else if (name.equals("Yellow Cake")) {
            GlideApp.with(holder.itemView)
                    .load(url)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.yellow_cake)
                    .fallback(R.drawable.yellow_cake)
                    .into(holder.recipeImage);
        } else {
            GlideApp.with(holder.itemView)
                    .load(url)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.cheescake)
                    .fallback(R.drawable.cheescake)
                    .into(holder.recipeImage);
        }
    }

    public String getNameForId(int position) {
        mCursor.moveToPosition(position - 1);
        return mCursor.getString(1);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface RecipeListAdapterOnClickHandler {
        void onClick(int recipeId);
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView recipeName;
        @BindView(R.id.tv_recipe_servings)
        TextView recipeServings;
        @BindView(R.id.iv_recipe_image)
        ImageView recipeImage;


        public RecipeListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int recipeId = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.ID));
            mClickHandler.onClick(recipeId);

        }
    }
}
