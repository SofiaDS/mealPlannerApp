package it.sds.mealplanner.model;

/**
 * A meal is a recipe that has been selected for a meal plan.
 * It has a corresponding recipe and a nutrition profile, which contains
 * information about the meal's nutritional facts.
 *
 * @author Sofia Della Spora
 * @version 1.0
 */
public class Meal {
    private final Recipe recipe;
    private final NutritionProfile nutritionProfile;

    public Meal(Recipe recipe, NutritionProfile nutritionProfile) {
        this.recipe = recipe;
        this.nutritionProfile = nutritionProfile;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
