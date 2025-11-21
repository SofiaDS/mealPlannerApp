package it.sds.mealplanner.model;

public class Ingredient {
    private String name;
    private Unit unit;
    private NutritionFacts nutritionPerUnit;

    public Ingredient(String name, Unit unit) {
        this.name = name.trim(); // input sanitization
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    public NutritionFacts getNutritionPerUnit() {
        return nutritionPerUnit;
    }

    public void setNutritionPerUnit(NutritionFacts nutritionPerUnit) {
        this.nutritionPerUnit = nutritionPerUnit;
    }

    @Override
    public String toString() {
        return "Ingredient " +
                "name='" + name + '\'' +
                ", unit=" + unit;
    }
}
