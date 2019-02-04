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

import java.util.ArrayList;
import java.util.Arrays;

public class SelectRecepieViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private final String BAKING_RECIPIES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private MutableLiveData<ArrayList<Recepie>> recepieList;

    /**
     * constructor
     * @param application the application this is part of
     */
    public SelectRecepieViewModel(@NonNull Application application) {
        super(application);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(application.getApplicationContext());

        recepieList = new MutableLiveData<>();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BAKING_RECIPIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(BakingApp.APP_TAG, "Recipe list loaded");
                        Log.i(BakingApp.APP_TAG, response);

                        Gson gson = new Gson();
                        Recepie[] recepies = gson.fromJson(response, Recepie[].class);

                        recepieList.setValue(new ArrayList<>(Arrays.asList(recepies)));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * gets the recepie data
     * @return the recepie list
     */
    public MutableLiveData<ArrayList<Recepie>> getRecepieList() {
        return recepieList;
    }
}
