package it.sds.mealplanner.service;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;

import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.ShoppingList;
import it.sds.mealplanner.repository.RecipeRepository;

public class MealPlannerService {

    private final Pantry pantry;
    private final RecipeRepository recipeRepository;

    public MealPlannerService(Pantry pantry, RecipeRepository recipeRepository) {
        if (pantry == null) {
            throw new IllegalArgumentException("Pantry cannot be null");
        }
        if (recipeRepository == null) {
            throw new IllegalArgumentException("RecipeRepository cannot be null");

        }
        this.pantry = pantry;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Assegna un'ordine di cucina in base alla MealPlan Plan.
     * 
     * @param plan         The MealPlan Plan to assign the recipe to.
     *                     The plan can be null.
     * @param startDay     The day of the week to start the meal plan
     * @param type         The type of the meal to assign to the plan
     * @param recipe       The recipe to assign to the plan
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

    public boolean autoAssignFirstCookableRecipe(MealPlan plan, DayOfWeek startDay, MealType type) {
        Optional<Recipe> maybeRecipe = recipeRepository.findAll().stream().filter(pantry::canMakeRecipe).findFirst();

        if (maybeRecipe.isEmpty()) {
            return false;

        }
        Recipe recipe = maybeRecipe.get();

        return assignRecipeUsingPantry(plan, startDay, type, recipe, new ShoppingList());
    }
}
