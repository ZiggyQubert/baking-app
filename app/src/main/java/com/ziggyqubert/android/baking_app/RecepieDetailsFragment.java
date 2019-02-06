package com.ziggyqubert.android.baking_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class RecepieDetailsFragment extends Fragment {


    /**
     * constructor
     */
    public RecepieDetailsFragment() {
        // Required empty public constructor
    }


    /**
     * basic create view, note logic for filling the fragment is handeled in the activity
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recepie_details, container, false);
    }
}
