package it.sds.mealplanner.app;

import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Recipe;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MealPlannerApp {

    public static void main(String[] args) {
        //create recipes
        Recipe pastaCeci = Recipe.create("Pasta e ceci");
        Recipe oatmeal = Recipe.create("Oatmeal proteico");
        Recipe appleSnack = Recipe.create("Mela");
        Recipe bananaSnack = Recipe.create("Banana");
        Recipe orangeSnack = Recipe.create("Orange");
        Recipe nutsSnack = Recipe.create("Frutta secca");
        Recipe yogurtBreakfast = Recipe.create("Yogurt greco + frutta");
        Recipe fishVeg = Recipe.create("Orata con patate e insalata");
        Recipe cousVeg = Recipe.create("Cous cous con verdure di stagione");

        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
        DayPlan tuesday = new DayPlan(DayOfWeek.TUESDAY);

        monday.assignRecipe(MealType.BREAKFAST, oatmeal);
        monday.assignRecipe(MealType.SNACK, appleSnack);
        monday.assignRecipe(MealType.LUNCH, pastaCeci);
        monday.assignRecipe(MealType.SNACK, nutsSnack);
        monday.assignRecipe(MealType.DINNER, fishVeg);
        monday.assignRecipe(MealType.SNACK, nutsSnack);
        tuesday.assignRecipe(MealType.BREAKFAST, yogurtBreakfast);
        tuesday.assignRecipe(MealType.SNACK, orangeSnack);
        tuesday.assignRecipe(MealType.LUNCH, cousVeg);
        tuesday.assignRecipe(MealType.SNACK, bananaSnack);
        MealPlan plan = new MealPlan(LocalDate.now());
        plan.addDayPlan(monday);
        plan.addDayPlan(tuesday);

        System.out.println(plan);
    }
}
