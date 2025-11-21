package it.sds.mealplanner.model;

/**
 * @param calories per porzione
 * @param protein  grammi
 * @param carbs    grammi
 * @param fat      grammi
 */
public record NutritionFacts(double calories, double protein, double carbs, double fat) {

    public NutritionFacts {
        if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
            throw new IllegalArgumentException("Nutrition values cannot be negative");
        }
    }

    @Override
    public String toString() {
        return "NutritionFacts{" +
                "kcal=" + calories +
                ", protein=" + protein +
                ", carbs=" + carbs +
                ", fat=" + fat +
                '}';
    }
}
