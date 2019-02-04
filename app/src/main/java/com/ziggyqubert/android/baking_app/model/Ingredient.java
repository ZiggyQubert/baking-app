package com.ziggyqubert.android.baking_app.model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String ingredient;
    public String measure;
    public Float quantity;

    public String getIngredientString() {
        return quantity + " " + measure + " " + ingredient;
    }
}
