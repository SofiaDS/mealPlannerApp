package it.sds.mealplanner.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingList {

    private final Map<Ingredient, Double> items = new HashMap<>();

    public void addItem(Ingredient ingredient, double quantity) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        double current = items.getOrDefault(ingredient, 0.0);
        items.put(ingredient, current + quantity);
    }

    public void addAll(Map<Ingredient, Double> missing) {
        if (missing == null) {
            return;
        }
        for (Map.Entry<Ingredient, Double> entry : missing.entrySet()) {
            addItem(entry.getKey(), entry.getValue());
        }
    }

    public Map<Ingredient, Double> getItems() {
        return Collections.unmodifiableMap(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shopping list:\n");
        for (Map.Entry<Ingredient, Double> entry : items.entrySet()) {
            Ingredient ing = entry.getKey();
            double qty = entry.getValue();
            sb.append("- ")
                    .append(ing.getName())
                    .append(": ")
                    .append(qty)
                    .append(" ")
                    .append(ing.getUnit())
                    .append("\n");
        }
        return sb.toString();
    }
}
