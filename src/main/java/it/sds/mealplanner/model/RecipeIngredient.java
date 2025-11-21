package it.sds.mealplanner.model;

public class RecipeIngredient {

    private final Ingredient ingredient;
    private final double quantityRequired;

    public RecipeIngredient(Ingredient ingredient, double quantityRequired) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantityRequired <= 0) {
            throw new IllegalArgumentException("Quantity required must be positive");
        }
        this.ingredient = ingredient;
        this.quantityRequired = quantityRequired;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "ingredient=" + ingredient +
                ", quantityRequired=" + quantityRequired +
                '}';
    }
}
