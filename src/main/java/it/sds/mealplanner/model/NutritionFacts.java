package it.sds.mealplanner.model;

public class NutritionFacts {

    private final double calories;      // per porzione
    private final double protein;       // grammi
    private final double carbs;         // grammi
    private final double fat;           // grammi

    public NutritionFacts(double calories,
                          double protein,
                          double carbs,
                          double fat) {
        if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
            throw new IllegalArgumentException("Nutrition values cannot be negative");
        }
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
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
