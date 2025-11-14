package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayPlan {

    private final DayOfWeek day;
    private final List<MealSlot> meals;

    public DayPlan(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        this.day = day;
        this.meals = new ArrayList<>();

        // struttura fissa
        meals.add(new MealSlot(day, MealType.BREAKFAST, null));
        meals.add(new MealSlot(day, MealType.SNACK, null));
        meals.add(new MealSlot(day, MealType.LUNCH, null));
        meals.add(new MealSlot(day, MealType.SNACK, null));
        meals.add(new MealSlot(day, MealType.DINNER, null));
    }

    public DayOfWeek getDay() {
        return day;
    }

    public List<MealSlot> getMeals() {
        return Collections.unmodifiableList(meals);
    }

    public boolean tryAssignRecipe(MealType type, Recipe recipe) {
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }

        if (type != MealType.SNACK) {
            for (MealSlot slot : meals) {
                if (slot.getType() == type) {
                    slot.setRecipe(recipe);
                    return true;
                }
            }
            return false;
        } else {
            for (MealSlot slot : meals) {
                if (slot.getType() == MealType.SNACK && slot.getRecipe() == null) {
                    slot.setRecipe(recipe);
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(day).append("\n");
        for (MealSlot slot : meals) {
            sb.append("  ").append(slot).append("\n");
        }
        return sb.toString();
    }
}
