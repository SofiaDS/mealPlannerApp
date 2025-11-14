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
        Recipe two = Recipe.create("Yogurt greco + frutta");
        Recipe three = Recipe.create("Orata con patate e insalata");
        Recipe four = Recipe.create("Mela");
        Recipe five = Recipe.create("Cous cous con verdure di stagione");
        //create mealSlot
        MealSlot mondayLunch = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH, pastaCeci);
        MealSlot mondayBreakfast = new MealSlot(DayOfWeek.MONDAY, MealType.BREAKFAST, oatmeal);
        MealSlot mondaySnack = new MealSlot(DayOfWeek.MONDAY, MealType.SNACK, two);
        MealSlot mondaySnack2 = new MealSlot(DayOfWeek.MONDAY, MealType.SNACK, four);
        MealSlot mondayDinner = new MealSlot(DayOfWeek.MONDAY, MealType.DINNER, three);
        MealSlot tuesdayLunch = new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH, five);
        //create day plan
        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
        monday.addMeal(mondayBreakfast);
        monday.addMeal(mondayLunch);
        monday.addMeal(mondaySnack);
        monday.addMeal(mondaySnack2);
        monday.addMeal(mondayDinner);
        DayPlan tuesday = new DayPlan(DayOfWeek.TUESDAY);
        tuesday.addMeal(tuesdayLunch);
        //create mealplan
        MealPlan plan = new MealPlan(LocalDate.now());
        plan.addDayPlan(monday);
        plan.addDayPlan(tuesday);

        //System.out.println("Meal plan:");
        //System.out.println(plan);
        System.out.println("\nAll meals in the plan:");
        for (MealSlot slot : plan.getAllMeals()) {
            System.out.println(slot.getDay() + ": " + slot);
        }
        System.out.println("\nAll meals in the plan (using Iterable):");
        for (MealSlot slot : plan) {
            System.out.println(slot.getDay() + ": " + slot);
        }
    }
}
