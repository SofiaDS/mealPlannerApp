package it.sds.mealplanner.model;

public class PantryItem {

    private final Ingredient ingredient;
    private double quantityAvailable;

    public PantryItem(Ingredient ingredient, double quantityAvailable) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negative");
        }
        this.ingredient = ingredient;
        this.quantityAvailable = quantityAvailable;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantityAvailable() {      // 👈 questo è il metodo che Pantry si aspetta
        return quantityAvailable;
    }

    public void setQuantityAvailable(double quantityAvailable) {
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negative");
        }
        this.quantityAvailable = quantityAvailable;
    }

    @Override
    public String toString() {
        return "PantryItem{" +
                "ingredient=" + ingredient +
                ", quantityAvailable=" + quantityAvailable +
                '}';
    }
}
