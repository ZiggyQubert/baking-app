package com.ziggyqubert.android.baking_app;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.utilities.RequestCallbacks;
import com.ziggyqubert.android.baking_app.utilities.SimpleIdlingResource;

import java.util.ArrayList;

public class SelectRecepieActivity extends AppCompatActivity
        implements SelectRecepieAdapter.SelectRecepieOnClickHandler {

    private SelectRecepieAdapter selectRecepieAdapter;


    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipe);

        getIdlingResource();

        //sets up variables
        SelectRecepieViewModel viewModel = getViewModel();

        //sets the status handeler for the view model
        final ProgressBar loadingSpinner = findViewById(R.id.recepies_loading_spinner);
        final TextView errorMessage = findViewById(R.id.recepies_error_message);
        viewModel.setObserver(new RequestCallbacks() {
            @Override
            public void onSuccess(ArrayList<Recepie> recepies) {
                loadingSpinner.setVisibility(View.GONE);
                mIdlingResource.setIdleState(true);
            }

            @Override
            public void onError(VolleyError error) {
                loadingSpinner.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
                mIdlingResource.setIdleState(true);
            }
        });

        //sets up teh recycler view
        MutableLiveData<ArrayList<Recepie>> recepieList = viewModel.getRecepieList();
        RecyclerView selectRecipieRecyclerView = findViewById(R.id.rv_recipe_display);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getScreenColumns());
        selectRecipieRecyclerView.setLayoutManager(layoutManager);
        selectRecipieRecyclerView.setHasFixedSize(true);
        selectRecepieAdapter = new SelectRecepieAdapter(this);
        selectRecipieRecyclerView.setAdapter(selectRecepieAdapter);

        //hooks into the live data for the recipie data
        recepieList.observe(this, new Observer<ArrayList<Recepie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recepie> recepies) {
                selectRecepieAdapter.addContent(recepies);
                findViewById(R.id.recepies_loading_spinner).setVisibility(View.GONE);
            }
        });
    }

    /**
     * calculates the number of columns to display on the screen
     *
     * @return number of columns to display
     */
    private Integer getScreenColumns() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int numberOfColumns = (int) Math.ceil(dpWidth / 500);
        Log.i(BakingApp.APP_TAG, "Set number of columns to: " + numberOfColumns);
        return numberOfColumns;
    }

    /**
     * gets the view model
     *
     * @return the view model
     */
    private SelectRecepieViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SelectRecepieViewModel.class);
    }

    /**
     * to be called when a recepie is selected in the recycler view, takes the user to the recepie page
     *
     * @param selectedRecepie the selected recepie
     */
    @Override
    public void onSelectRecepie(Recepie selectedRecepie) {
        Log.i(BakingApp.APP_TAG, "Recepie selected: " + selectedRecepie.getName());

        Context context = this;
        Class destinationClass = RecepieDisplayActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_SUBJECT, selectedRecepie);

        startActivity(intentToStartDetailActivity);
    }
}


