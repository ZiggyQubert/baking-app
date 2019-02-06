package com.ziggyqubert.android.baking_app;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.utilities.DataLoader;
import com.ziggyqubert.android.baking_app.utilities.RequestCallbacks;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectRecepieViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private final String BAKING_RECIPIES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private MutableLiveData<ArrayList<Recepie>> recepieList;
    private RequestCallbacks statusCallbacks;

    /**
     * constructor
     *
     * @param application the application this is part of
     */
    public SelectRecepieViewModel(@NonNull Application application) {
        super(application);

        // Instantiate the RequestQueue.

        recepieList = new MutableLiveData<>();

        DataLoader.loadData(new RequestCallbacks() {
            @Override
            public void onSuccess(ArrayList<Recepie> recepies) {
                recepieList.setValue(recepies);
                if (statusCallbacks != null) {
                    statusCallbacks.onSuccess(recepies);
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (statusCallbacks != null) {
                    statusCallbacks.onError(error);
                }
            }
        });
    }

    public void setObserver(final RequestCallbacks callbacks) {
        statusCallbacks = callbacks;
    }

    /**
     * gets the recepie data
     *
     * @return the recepie list
     */
    public MutableLiveData<ArrayList<Recepie>> getRecepieList() {
        return recepieList;
    }
}
