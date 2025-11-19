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

  public boolean tryAssignRecipe(MealType type, Recipe recipe) {
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
      System.out.println("    [DayPlan] tryAssignRecipe day=" + day
              + " type=" + type
              + " recipe=" + recipe.getName());

        if (type == MealType.SNACK) {
            for (MealSlot slot : meals) {
                if (slot.getType() == MealType.SNACK && slot.getRecipe() == null) {
                    System.out.println("    [DayPlan]  -> assegno a SNACK slot (day=" + day + ")");
                    slot.setRecipe(recipe);
                    return true;
                }
            }
            System.out.println("    [DayPlan]  -> nessuno SNACK libero per " + day);
            return false;
        }

        for (MealSlot slot : meals) {
            if (slot.getType() == type) {
                System.out.println("    [DayPlan]  -> assegno a " + type + " (day=" + day + ")");
                slot.setRecipe(recipe);
                return true;
            }
        }
      System.out.println("    [DayPlan]  -> nessuno slot per type=" + type + " su day=" + day);
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
