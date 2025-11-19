package it.sds.mealplanner.app;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Recipe;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MainTest {
        public static void main(String[] args) {
            MealPlan plan = new MealPlan(LocalDate.now());
            plan.addDayPlan(new     DayPlan(DayOfWeek.MONDAY));

            Recipe r = Recipe.create("DEBUG RECIPE", MealType.BREAKFAST);
            DayPlan monday = plan.getDayPlan(DayOfWeek.MONDAY);
            monday.tryAssignRecipe(MealType.BREAKFAST, r);

            System.out.println("DEBUG MEAL PLAN (RUN 1):");

            for (MealSlot slot : monday.getMeals()) {
                System.out.println(slot.getType() + " -> " +
                        (slot.getRecipe() != null ? slot.getRecipe().getName() : "(vuoto)"));
            }
        }
    }


