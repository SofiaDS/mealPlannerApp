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
     * Assigns a recipe to the meal plan starting from the given day.
     * First, it checks if the pantry has enough ingredients to make the recipe.
     * If not, it adds the missing ingredients to the shopping list and returns
     * false.
     * If the pantry has enough ingredients, it consumes them and assigns the recipe
     * to the meal plan.
     * 
     * @param plan         the meal plan to assign the recipe to
     * @param startDay     the day to start searching from
     * @param type         the meal type to assign the recipe to
     * @param recipe       the recipe to assign
     * @param shoppingList the shopping list to add the missing ingredients to
     * @return true if the recipe was assigned, false if not enough ingredients were
     *         found
     * @throws IllegalArgumentException if any of the parameters are null
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

        // calc missing ingredients before consuming
        Map<Ingredient, Double> missing = pantry.calculateMissingForRecipe(recipe);

        if (!missing.isEmpty()) {
            // not enough: update the shopping list, do not consume or assign
            shoppingList.addAll(missing);
            return false;
        }

        // pantry is sufficient: consume and assign
        pantry.consumeForRecipe(recipe);
        plan.assignRecipeAuto(startDay, type, recipe);
        return true;
    }

    /**
     * Convenience method that calls assignRecipeUsingPantry with an empty
     * ShoppingList.
     * 
     * @param plan     the meal plan to assign the recipe to
     * @param startDay the day to start searching from
     * @param type     the meal type to assign the recipe to
     * @param recipe   the recipe to assign
     * @return true if the recipe was assigned, false if not enough ingredients were
     *         found
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public boolean assignRecipeUsingPantry(MealPlan plan,
            DayOfWeek startDay,
            MealType type,
            Recipe recipe) {
        return assignRecipeUsingPantry(plan, startDay, type, recipe, new ShoppingList());
    }

    /**
     * Automatically assigns the first cookable recipe from the repository
     * to the meal plan starting from the given day and meal type.
     * 
     * @param plan     the meal plan to assign the recipe to
     * @param startDay the day to start searching from
     * @param type     the meal type to assign the recipe to
     * @return true if a recipe was assigned, false if no cookable recipe was found
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public boolean autoAssignFirstCookableRecipe(MealPlan plan,
            DayOfWeek startDay,
            MealType type) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }

        Optional<Recipe> maybeRecipe = recipeRepository.findAll()
                .stream()
                .filter(pantry::canMakeRecipe)
                .findFirst();

        if (maybeRecipe.isEmpty()) {
            return false;
        }

        Recipe recipe = maybeRecipe.get();
        return assignRecipeUsingPantry(plan, startDay, type, recipe);
    }
}
