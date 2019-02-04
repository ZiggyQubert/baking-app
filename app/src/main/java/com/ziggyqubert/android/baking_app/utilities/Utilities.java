package com.ziggyqubert.android.baking_app.utilities;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ziggyqubert.android.baking_app.BakingApp;
import com.ziggyqubert.android.baking_app.R;
import com.ziggyqubert.android.baking_app.model.Recepie;

public class Utilities {

    /**
     * utility function to display a recepie image in an image view using picasso, standardizes the placeholder and error handeling
     * @param recepie the recepie to use
     * @param imageView the image view to place the image in
     */
    public static void displayrecepieImage(Recepie recepie, ImageView imageView) {
        Picasso.get()
                .load(recepie.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.recepie_not_found)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(BakingApp.APP_TAG, "Error loading image");
                        e.printStackTrace();
                    }
                });
    }

}
