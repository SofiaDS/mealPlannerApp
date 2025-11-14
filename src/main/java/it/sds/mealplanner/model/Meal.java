package it.sds.mealplanner.model;
/*
* Meal will have to respect a certain amount of nutritionProfile values
* */
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

    public NutritionProfile getNutritionProfile() {
        return nutritionProfile;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
