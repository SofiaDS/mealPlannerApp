package it.sds.mealplanner.service;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.Map;

public class MealPlannerService {

    private final Pantry pantry;

    public MealPlannerService(Pantry pantry) {
        if (pantry == null) {
            throw new IllegalArgumentException("Pantry cannot be null");
        }
        this.pantry = pantry;
    }

    // VERSIONE "SIMPLE": usa solo la dispensa, niente shopping list
    public boolean assignRecipeUsingPantry(MealPlan plan,
                                           DayOfWeek startDay,
                                           MealType type,
                                           Recipe recipe) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        // se non ce la facciamo, NON consumiamo e NON assegnamo
        if (!pantry.canMakeRecipe(recipe)) {
            return false;
        }

        // qui siamo sicuri che basta
        pantry.consumeForRecipe(recipe);
        plan.assignRecipeAuto(startDay, type, recipe);
        return true;
    }

    // VERSIONE "COMPLETA": se mancano ingredienti, aggiorna la shopping list
    public boolean assignRecipeUsingPantry(MealPlan plan,
                                           DayOfWeek startDay,
                                           MealType type,
                                           Recipe recipe,
                                           ShoppingList shoppingList) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        if (shoppingList == null) {
            throw new IllegalArgumentException("ShoppingList cannot be null");
        }

        // calcolo cosa manca PRIMA di consumare
        Map<Ingredient, Double> missing = pantry.calculateMissingForRecipe(recipe);

        if (!missing.isEmpty()) {
            // non basta: aggiorno la shopping list, NON consumo, NON assegno
            shoppingList.addAll(missing);
            return false;
        }

        // qui so che non manca nulla
        pantry.consumeForRecipe(recipe);
        plan.assignRecipeAuto(startDay, type, recipe);
        return true;
    }
}
