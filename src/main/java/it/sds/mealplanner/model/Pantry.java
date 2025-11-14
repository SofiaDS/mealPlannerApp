package it.sds.mealplanner.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Pantry {

    private final Map<Ingredient, PantryItem> items = new HashMap<>();

    /**
     * Add a given quantity of a given ingredient to the pantry
     * @param ingredient the ingredient to add
     * @param quantity the quantity of the ingredient to add
     * @throws IllegalArgumentException if the ingredient is null or if the quantity is not positive
     */
    public void addStock(Ingredient ingredient, double quantity) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        PantryItem existing = items.get(ingredient);
        if (existing == null) {
            items.put(ingredient, new PantryItem(ingredient, quantity));
        } else {
            double newQuantity = existing.getQuantityAvailable() + quantity;
            existing.setQuantityAvailable(newQuantity);
        }
    }


    /**
     * Returns the available quantity of a given ingredient in the pantry.
     * If the ingredient is not present in the pantry, returns 0.0.
     * @param ingredient the ingredient to check
     * @return the available quantity of the ingredient, or 0.0 if not present
     */
    public double getAvailableQuantity(Ingredient ingredient) {
        PantryItem item = items.get(ingredient);
        return (item == null) ? 0.0 : item.getQuantityAvailable();
    }

    public Map<Ingredient, PantryItem> getItems() {
        return Collections.unmodifiableMap(items);
    }


    /**
     * Check if the pantry contains sufficient ingredients to make a given recipe.
     * @param recipe the recipe to check
     * @return true if the pantry contains sufficient ingredients, false otherwise
     */
    public boolean canMakeRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        for (RecipeIngredient ri : recipe.getIngredients()) {
            double available = getAvailableQuantity(ri.getIngredient());
            if (available < ri.getQuantityRequired()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consumes the necessary ingredients from the pantry to make a given recipe.
     * @param recipe the recipe to make
     * @throws IllegalStateException if the pantry does not contain enough ingredients to make the recipe
     */
    public void consumeForRecipe(Recipe recipe) {
        if (canMakeRecipe(recipe)) {
            throw new IllegalStateException(
                    "Not enough ingredients in pantry to make recipe: " + recipe.getName()
            );
        }

        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            PantryItem item = items.get(ing);
            double newQuantity = item.getQuantityAvailable() - ri.getQuantityRequired();
            item.setQuantityAvailable(newQuantity);
        }
    }

    /**
     * Returns a map of ingredients to the quantity of each ingredient that is missing from the pantry
     * in order to make the given recipe.
     * @param recipe the recipe to check
     * @return a map of ingredients to the quantity of each ingredient that is missing from the pantry
     */
    public Map<Ingredient, Double> calculateMissingForRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        Map<Ingredient, Double> missing = new HashMap<>();
        for (RecipeIngredient ri : recipe.getIngredients()) {
            double available = getAvailableQuantity(ri.getIngredient());
            double required = ri.getQuantityRequired();
            if (available < required) {
                double need = required - available;
                missing.put(ri.getIngredient(), need);
            }
        }
        return missing;
    }

}
