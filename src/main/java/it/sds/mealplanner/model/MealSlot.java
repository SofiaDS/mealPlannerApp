package it.sds.mealplanner.model;

import java.time.DayOfWeek;

public class MealSlot {

    private final DayOfWeek day;
    private final MealType type;
    private Recipe recipe;

    public MealSlot(DayOfWeek day, MealType type, Recipe recipe){
        if (day == null){
            throw new IllegalArgumentException("Day cannot be null");
        }
        if (type == null){
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        this.day = day;
        this.type = type;
        this.recipe = recipe;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public MealType getType() {
        return type;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return  type +
                " = " + (recipe != null ? recipe.getName() : "EMPTY");
    }
}
