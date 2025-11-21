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

        // ordine fisso: B, S, L, S, D
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

    /**
     * Assigns a recipe to a meal slot of the given type in the day plan.
     * If the given type is SNACK, it will try to assign the recipe to the first
     * available SNACK slot. If no SNACK slot is available, it will return false.
     * If the given type is not SNACK, it will try to assign the recipe to the first
     * available slot of the given type. If no slot is available, it will return
     * false.
     * 
     * @param type   the meal type of the slot to assign the recipe to
     * @param recipe the recipe to assign
     * @return true if the recipe was assigned, false otherwise
     */
    public boolean tryAssignRecipe(MealType type, Recipe recipe) {
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        if (type == MealType.SNACK) {
            for (MealSlot slot : meals) {
                if (slot.getType() == MealType.SNACK && slot.getRecipe() == null) {
                    // System.out.println(" [DayPlan] -> assegno a SNACK slot (day=" + day + ")");
                    slot.setRecipe(recipe);
                    return true;
                }
            }
            // System.out.println(" [DayPlan] -> nessuno SNACK libero per " + day);
            return false;
        }

        for (MealSlot slot : meals) {
            if (slot.getType() == type) {
                // System.out.println(" [DayPlan] -> assegno a " + type + " (day=" + day + ")");
                slot.setRecipe(recipe);
                return true;
            }
        }
        // System.out.println(" [DayPlan] -> nessuno slot per type=" + type + " su day="
        // + day);
        return false;
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
