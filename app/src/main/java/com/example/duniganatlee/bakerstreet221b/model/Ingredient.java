package com.example.duniganatlee.bakerstreet221b.model;

public class Ingredient {
    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {}

    /* Getters and setters. */
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getFullDescription() {
        return quantity + " " + measure + " " + ingredient;
    }
}
