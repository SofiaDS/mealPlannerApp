package it.sds.mealplanner.model;

public class NutritionProfile {
    private final double protein;
    private final double carbs;
    private final double fats;
    private final double kcal;

    public NutritionProfile(double protein, double carbs, double fats, double kcal) {
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.kcal = kcal;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public double getKcal() {
        return kcal;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
