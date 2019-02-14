package com.ziggyqubert.android.baking_app;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.ziggyqubert.android.baking_app.model.Ingredient;
import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RecepieDisplayActivityTest {

    @Rule
    public ActivityTestRule<RecepieDisplayActivity> mActivityTestRule = new ActivityTestRule<>(RecepieDisplayActivity.class, false, false);


    @Before
    public void loadRecepieDisplayActivity() {
        Recepie mockObject = new Recepie();
        mockObject.id = 0;
        mockObject.name = "Mock Recepie";
        mockObject.servings = 4;
        mockObject.ingredients = new Ingredient[0];
        mockObject.steps = new Step[0];

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SUBJECT, mockObject);

        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void recepieData_isLoaded() {
        onView(withId(R.id.tv_servings_value)).check(matches(withText("4")));
    }
}