package it.sds.mealplanner.model;

import java.time.DayOfWeek;

/**
 * Hello world!
 *
 */
public class App 
{
    static void main( String[] args )
    {   Recipe r0 = Recipe.create("Yogurt greco e banana");
        Recipe r1 = Recipe.create("Pasta e ceci");
        Recipe r2 = Recipe.create("Frutta");
        Recipe r3 = Recipe.create("Orata con patate e insalata");
        Recipe r4 = Recipe.create("Porridge");
        MealSlot slot0 = new MealSlot(DayOfWeek.MONDAY, MealType.BREAKFAST, r4);
        MealSlot slot1 = new MealSlot(DayOfWeek.MONDAY, MealType.SNACK, r0);
        MealSlot slot2 = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH, r1);
        MealSlot slot3 = new MealSlot(DayOfWeek.MONDAY, MealType.SNACK, r2);
        MealSlot slot4 = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH, r3);

        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);

        monday.addMeal(slot1);
        monday.addMeal(slot2);
        monday.addMeal(slot3);
        monday.addMeal(slot0);
        monday.addMeal(slot4);
        System.out.println(monday);

    }
}
