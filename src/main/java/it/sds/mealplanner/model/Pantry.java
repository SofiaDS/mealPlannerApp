package it.sds.mealplanner.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Pantry {

    private final Map<Ingredient, PantryItem> items = new HashMap<>();

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

    public double getAvailableQuantity(Ingredient ingredient) {
        PantryItem item = items.get(ingredient);
        return (item == null) ? 0.0 : item.getQuantityAvailable();
    }

    public Map<Ingredient, PantryItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public boolean canMakeRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        for (RecipeIngredient ri : recipe.getIngredients()) {
            double available = getAvailableQuantity(ri.getIngredient());
            double required = ri.getQuantityRequired();
            if (available < required) {
                return false;
            }
        }
        return true;
    }

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

    public void consumeForRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        if (!canMakeRecipe(recipe)) {
            throw new IllegalStateException(
                    "Not enough ingredients in pantry to make recipe: " + recipe.getName());
        }

        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            PantryItem item = items.get(ing);
            double newQuantity = item.getQuantityAvailable() - ri.getQuantityRequired();
            item.setQuantityAvailable(newQuantity);
        }
    }
}
