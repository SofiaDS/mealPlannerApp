package it.sds.mealplanner.model;

import java.time.DayOfWeek;

/**
 * Hello world!
 *
 */
public class App 
{
    static void main( String[] args )
    {
        Recipe r1 = Recipe.create("Pasta e ceci");
        MealSlot slot = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH, r1);
        System.out.println(slot);

    }
}
