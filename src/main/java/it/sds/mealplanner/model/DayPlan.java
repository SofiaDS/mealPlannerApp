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

/**
 * Returns an unmodifiable list of MealSlot objects that represent the plan for the day.
 * @return an unmodifiable list of MealSlot objects
 */
    public List<MealSlot> getMeals() {
        return Collections.unmodifiableList(meals);
    }

    /**
     * Tries to assign the given recipe to the meal slot of the given meal type.
     * If the meal type is not SNACK, the method tries to assign the recipe to the first meal slot of the given meal type.
     * If the meal type is SNACK, the method tries to assign the recipe to the first meal slot of the SNACK meal type that does not already have a recipe assigned.
     * If a suitable meal slot is found, the method assigns the recipe to the meal slot and returns true.
     * Otherwise, the method returns false.
     * @param type the meal type to assign the recipe to
     * @param recipe the recipe to assign
     * @return true if a suitable meal slot was found and the recipe was assigned, false otherwise
     * @throws IllegalArgumentException if type is null
     **/
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
