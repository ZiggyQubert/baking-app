package com.ziggyqubert.android.baking_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.model.Step;
import com.ziggyqubert.android.baking_app.utilities.Utilities;

/**
 * displays a passed recepie
 */
public class RecepieDisplayActivity extends AppCompatActivity
        implements StepDetailsFragment.OnFragmentInteractionListener {

    protected Recepie selectedRecepie;
    protected StepDetailsFragment stepDisplayFragment;
    protected Intent intentThatStartedThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recepie_display);

        intentThatStartedThisActivity = getIntent();
        Recepie passedRecepie = (Recepie) intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_SUBJECT);

        //if the corect data is not passed return to the previous screen
        if (passedRecepie == null) {
            showNoRecepieFound();
        } else {
            //checks to see if being displayed as a two frame layout, if so save a reference to the fragment
            if (findViewById(R.id.step_details_fragment) != null) {
                stepDisplayFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
            }
            //set up teh display data
            setRecepieData(passedRecepie);
        }
    }

    /**
     * basic error handeling for when data is not found
     */
    protected void showNoRecepieFound() {
        Toast.makeText(this, getText(R.string.no_recepie_found), Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * sets and displays the recepie data in the view
     *
     * @param recepie the recepie to display
     */
    protected void setRecepieData(Recepie recepie) {
        Log.i(BakingApp.APP_TAG, "Display recepie: " + recepie.getName());
        selectedRecepie = recepie;

        //sets teh page title
        setTitle(selectedRecepie.getName());

        //sets teh static text
        Utilities.displayrecepieImage(recepie, (ImageView) findViewById(R.id.iv_recepie_image));
        ((TextView) findViewById(R.id.tv_servings_value)).setText(selectedRecepie.getServings().toString());
        ((TextView) findViewById(R.id.tv_ingridentList)).setText(selectedRecepie.getIngredientsText());

        //builds and displays the steps list
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout list = findViewById(R.id.ll_step_display);
        for (int i = 0; i < selectedRecepie.steps.length; i++) {
            Step step = selectedRecepie.steps[i];
            View stepView = layoutInflater.inflate(R.layout.step_list_item, null);
            stepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer position = ((ViewGroup) view.getParent()).indexOfChild(view);
                    onItemClick(position);
                }
            });

            ((TextView) stepView.findViewById(R.id.label)).setText(step.getShortDescription());
            ImageView icon = stepView.findViewById(R.id.icon);
            if (step.hasVideo()) {
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }
            list.addView(stepView);
        }

        //if a two panel display then set up the second panel
        if (stepDisplayFragment != null) {
            Integer currentStep = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_UID, 0);
            stepDisplayFragment.setStepData(selectedRecepie.steps[currentStep], true);
        }

    }

    /**
     * when a step is clicked update the correct fragment or move to the step activity
     *
     * @param position position of the clicked item
     */
    public void onItemClick(int position) {
        Step step = selectedRecepie.steps[position];
        Log.i(BakingApp.APP_TAG, "Selected step:" + position + " " + step.getShortDescription());

        if (stepDisplayFragment != null) {
            intentThatStartedThisActivity.putExtra(Intent.EXTRA_UID, position);
            stepDisplayFragment.setStepData(selectedRecepie.steps[position], true);
        } else {
            Context context = this;
            Class destinationClass = StepDisplayActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_SUBJECT, selectedRecepie);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_UID, position);

            startActivity(intentToStartDetailActivity);
        }
    }

    /**
     * empty as we dont use this interaction in this activity
     *
     * @param navigateToNext should we navigate to next
     */
    @Override
    public void onSelectNextButton(Boolean navigateToNext) {

    }
}
