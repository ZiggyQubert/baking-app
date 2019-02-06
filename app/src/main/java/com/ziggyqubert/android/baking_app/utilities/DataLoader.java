package com.ziggyqubert.android.baking_app.utilities;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ziggyqubert.android.baking_app.BakingApp;
import com.ziggyqubert.android.baking_app.model.Recepie;

import java.util.ArrayList;
import java.util.Arrays;

public class DataLoader {

    private static final String BAKING_RECIPIES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static RequestQueue queue;
    private static ArrayList<Recepie> cachedRecepieList;

    /**
     * processes a url request
     *
     * @param url
     * @param responseListner
     * @param responseErrorListner
     */
    public static void loadRawData(String url, Response.Listener responseListner, Response.ErrorListener responseErrorListner) {

        if (queue == null) {
            queue = Volley.newRequestQueue(BakingApp.getAppContext().getApplicationContext());
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListner, responseErrorListner);

        queue.add(stringRequest);
    }

    /**
     * gets the recepie data and returns the parsed object
     *
     * @param callbacks
     */
    public static void loadData(final RequestCallbacks callbacks) {

        if (cachedRecepieList != null) {
            callbacks.onSuccess(cachedRecepieList);
        } else {

            loadRawData(BAKING_RECIPIES_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(BakingApp.APP_TAG, "Recipe list loaded");
                            Log.i(BakingApp.APP_TAG, response);

                            Gson gson = new Gson();
                            Recepie[] recepies = gson.fromJson(response, Recepie[].class);

                            cachedRecepieList = new ArrayList<>(Arrays.asList(recepies));
                            callbacks.onSuccess(cachedRecepieList);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callbacks.onError(error);
                        }
                    });
        }
    }
}
