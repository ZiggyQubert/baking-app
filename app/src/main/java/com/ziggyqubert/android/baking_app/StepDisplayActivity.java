package com.ziggyqubert.android.baking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ziggyqubert.android.baking_app.model.Recepie;

/**
 * displays a single step as an activity
 */
public class StepDisplayActivity extends AppCompatActivity
        implements StepDetailsFragment.OnFragmentInteractionListener {

    protected StepDetailsFragment stepDisplayFragment;
    Recepie passedRecepie;
    Integer stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_display);

        Intent intentThatStartedThisActivity = getIntent();
        passedRecepie = (Recepie) intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_SUBJECT);
        stepIndex = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_UID, 0);

        //if the correct data is not passed retrun to the previous screen
        if (passedRecepie == null || stepIndex == null) {
            showNoStepFound();
        } else {
            setTitle(passedRecepie.getName());

            stepDisplayFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
            displayStepInFragment();
        }
    }

    /**
     * basic error handeling for when data is not found
     */
    protected void showNoStepFound() {
        Toast.makeText(this, getText(R.string.no_step_found), Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * sets the recepie info in the fragment
     */
    protected void displayStepInFragment() {
        stepDisplayFragment.setStepData(passedRecepie.steps[stepIndex], passedRecepie.steps.length - 1 <= stepIndex);
    }

    /**
     * for teh fragment interaction, to hendel clicking the next botton
     *
     * @param navigateToNext should navigate
     */
    @Override
    public void onSelectNextButton(Boolean navigateToNext) {
        if (navigateToNext) {
            stepIndex++;
            displayStepInFragment();
            Intent intentThatStartedThisActivity = getIntent();
            intentThatStartedThisActivity.putExtra(Intent.EXTRA_UID, stepIndex);
        }
    }
}
