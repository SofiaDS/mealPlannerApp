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

/**
 * Assegna un'ordine di cucina in base alla MealPlan Plan. 
 * @param plan The MealPlan Plan to assign the recipe to.
.
The plan can be null.
 * @param startDay The day of the week to start the meal plan
 * @param type The type of the meal to assign to the plan
 * @param recipe The recipe to assign to the plan
 * @param shoppingList The shopping list to assign to the plan
 * @return true if the assignment was successful, false otherwise
 */
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

        Map<Ingredient, Double> missing = pantry.calculateMissingForRecipe(recipe);

        if (!missing.isEmpty()) {
            shoppingList.addAll(missing);
            return false;
        }

        pantry.consumeForRecipe(recipe);
        plan.assignRecipeAuto(startDay, type, recipe);
        return true;
    }
}
