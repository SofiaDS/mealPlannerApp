package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class App 
{
    static void main( String[] args )
    {
        Recipe r1 = Recipe.create("Pasta e ceci");
        MealSlot mondayLunch = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH, r1);

        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
        monday.addMeal(mondayLunch);

        MealPlan plan = new MealPlan(LocalDate.now());
        plan.addDayPlan(monday);

        System.out.println(plan);


    }
}
