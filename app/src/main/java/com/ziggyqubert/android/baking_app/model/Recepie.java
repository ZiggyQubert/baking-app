package com.ziggyqubert.android.baking_app.model;

import com.ziggyqubert.android.baking_app.R;

import java.io.Serializable;

public class Recepie implements Serializable {
    public Integer id;
    public String name;
    public String image;
    public Integer servings;
    public Ingredient[] ingredients;
    public Step[] steps;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        if (image.isEmpty()) {
            String keyWords = name.replaceAll(" ", ",");
            //using https://loremflickr.com/ to generate an image when one is missing
            return "https://loremflickr.com/400/400/" + keyWords + "/all?lock=" + id;
        } else {
            return image;
        }
    }

    public Integer getServings() {
        return servings;
    }

    public String getIngredientsText() {
        String ingridentsString = "";

        for (Integer ingIdx = 0; ingIdx < ingredients.length; ingIdx++) {
            ingridentsString += ingredients[ingIdx].getIngredientString() + "\n";
        }
        return ingridentsString;
    }

    public String getStepShortText() {
        String stepsString = "";

        for (Integer ingIdx = 0; ingIdx < steps.length; ingIdx++) {
            stepsString += steps[ingIdx].getShortDescription() + "\n";
        }
        return stepsString;
    }
}
