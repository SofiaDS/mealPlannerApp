package it.sds.mealplanner.service;
import it.sds.mealplanner.model.*;
import java.time.DayOfWeek;

public class MealPlannerService {

    private final Pantry pantry;

    public MealPlannerService(Pantry pantry) {
        if (pantry == null) {
            throw new IllegalArgumentException("Pantry cannot be null");
        }
        this.pantry = pantry;
    }

    /**
     * Try to assign a recipe in a DayPlan MealSLot
     * - check if in Pantry there are enough ingredients
     * - Y -> remove ingredients from Pantry
     *      then use MealPlan.assignRecipeAuto to find the righ day/slot
     * - N -> TBD false
     */
    public boolean assignRecipeUsingPantry(MealPlan plan, DayOfWeek startDay, MealType type, Recipe recipe) {
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

        // 1) check if pantry can sustain a recipe
        if (!pantry.canMakeRecipe(recipe)) {
            return false; // To enhance in future
        }

        // 2) consume ingredients from Pantry
        pantry.consumeForRecipe(recipe);

        // 3) assign the recipe to the MealPlan
        plan.assignRecipeAuto(startDay, type, recipe);

        return true;
    }
}
