package it.sds.mealplanner.model;

public class PantryItem {

    private final Ingredient ingredient;
    private double quantityAvailable;

    public PantryItem(Ingredient ingredient, double quantityAvailable) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negat
