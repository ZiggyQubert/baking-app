package com.ziggyqubert.android.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.ziggyqubert.android.baking_app.BakingApp;
import com.ziggyqubert.android.baking_app.R;
import com.ziggyqubert.android.baking_app.RecepieDisplayActivity;
import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.utilities.DataLoader;
import com.ziggyqubert.android.baking_app.utilities.RequestCallbacks;
import com.ziggyqubert.android.baking_app.utilities.Utilities;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of App Widget functionality.
 */
public class FeaturedRecepieWidget extends AppWidgetProvider {

    protected static String TAG = BakingApp.APP_TAG + "_FEATURED_WIDGET";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {


        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.featured_recepie_widget);

        DataLoader.loadData(new RequestCallbacks() {
            @Override
            public void onSuccess(ArrayList<Recepie> recepies) {

                //generates a random recepie from the returned list and ensures tht its the same recepie all day
                Random rnd = new Random();
                rnd.setSeed(Utilities.dateToDays());
                int recepieNumber = rnd.nextInt(recepies.size());

                Recepie featuredRecepie = recepies.get(recepieNumber);
                Log.i(TAG, "Featured Recepie: " + featuredRecepie.getName());

                //setup and display the image
                int[] appWidgetIds = new int[1];
                appWidgetIds[0] = appWidgetId;
                Picasso.get()
                        .load(featuredRecepie.getImage())
                        .into(views, R.id.widget_background_image, appWidgetIds);

                views.setTextViewText(R.id.widget_featured_title, featuredRecepie.getName());
                views.setTextViewText(R.id.widget_featured_ingridents, featuredRecepie.getIngredientsText());

                //set up the click intent
                Intent intent = new Intent(context, RecepieDisplayActivity.class);
                intent.putExtra(Intent.EXTRA_SUBJECT, featuredRecepie);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_featured_container, pendingIntent);

                //update the widget UI
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onError(VolleyError error) {
                views.setViewVisibility(R.id.widget_error, View.VISIBLE);
                views.setViewVisibility(R.id.widget_background_image, View.GONE);
                views.setViewVisibility(R.id.widget_featured_container, View.GONE);
                appWidgetManager.updateAppWidget(appWidgetId, views);

            }
        });
        // Instruct the widget manager to update the widget
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

