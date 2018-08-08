package com.example.fajar.bakingapp.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fajar.bakingapp.R;
import com.example.fajar.bakingapp.database.RecipesProvider;
import com.example.fajar.bakingapp.ui.RecipeActivity;
import com.example.fajar.bakingapp.ui.adapter.IngredientsAdapter;
import com.example.fajar.bakingapp.ui.adapter.RecipeAdapter;
import com.example.fajar.bakingapp.ui.adapter.RecipeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final int ID_INGREDIENTS_LOADER = 12;
    private static final int ID_RECIPE_LOADER = 7;


    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecyclerViewRecipe;
    @BindView(R.id.rv_ingredients)
    RecyclerView mRecyclerViewIngredients;
    @BindView(R.id.iv_recipe_image_detail)
    ImageView mImageViewRecipe;


    private OnStepSelectedListener stepSelectedListener;
    private IngredientsAdapter mIngredientsAdapter;
    private RecipeAdapter mRecipeAdapter;
    private int recipeId;
    private Bundle args;

    public RecipeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);

        //get image for each recipe
        args = getActivity().getIntent().getExtras();
        String name = args.getString(RecipeListAdapter.RECIPE_NAME);
        if (name.equals("Nutella Pie")) {
            mImageViewRecipe.setImageResource(R.drawable.pie);
        } else if (name.equals("Brownies")) {
            mImageViewRecipe.setImageResource(R.drawable.brownies);
        } else if (name.equals("Yellow Cake")) {
            mImageViewRecipe.setImageResource(R.drawable.yellow_cake);
        } else {
            mImageViewRecipe.setImageResource(R.drawable.cheescake);
        }

        mIngredientsAdapter = new IngredientsAdapter();
        mRecyclerViewIngredients.setAdapter(mIngredientsAdapter);
        mRecyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                }
        );
        mRecyclerViewIngredients.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerViewRecipe.setAdapter(mRecipeAdapter);
        mRecyclerViewRecipe.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        mRecyclerViewRecipe.setHasFixedSize(true);

        recipeId = ((RecipeActivity) getActivity()).getRecipeId();

        getActivity().getSupportLoaderManager().initLoader(ID_INGREDIENTS_LOADER, null, this);
        getActivity().getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        stepSelectedListener = (OnStepSelectedListener) context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_INGREDIENTS_LOADER:
                Uri ingredientQueryUri = RecipesProvider.Ingredients.ingredientsForRecipe(recipeId);
                return new CursorLoader(getActivity(), ingredientQueryUri, null, null, null, null);

            case ID_RECIPE_LOADER:
                Uri recipeQueryUri = RecipesProvider.Steps.stepsForRecipe(recipeId);
                return new CursorLoader(getActivity(), recipeQueryUri, null, null, null, null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            switch (loader.getId()) {
                case ID_INGREDIENTS_LOADER:
                    mIngredientsAdapter.swapCursor(data);
                    break;

                case ID_RECIPE_LOADER:
                    mRecipeAdapter.swapCursor(data);
                    //Toast.makeText(this, "Count: " + mRecipeAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int stepId) {
        stepSelectedListener.onStepSelected(stepId);
    }


    public interface OnStepSelectedListener {
        void onStepSelected(int stepId);
    }
}
