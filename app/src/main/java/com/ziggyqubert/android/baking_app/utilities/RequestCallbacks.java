package com.ziggyqubert.android.baking_app.utilities;

import com.android.volley.VolleyError;
import com.ziggyqubert.android.baking_app.model.Recepie;

import java.util.ArrayList;

public interface RequestCallbacks {
    void onSuccess(ArrayList<Recepie> recepies);

    void onError(VolleyError error);
}
