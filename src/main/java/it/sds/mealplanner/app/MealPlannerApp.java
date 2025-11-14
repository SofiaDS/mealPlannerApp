package it.sds.mealplanner.app;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.service.MealPlannerService;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MealPlannerApp {

    public static void main(String[] args) {

        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        Ingredient ceci = new Ingredient("Ceci", Unit.GRAM);

        Recipe bigPastaCeci = Recipe.create("Pasta e ceci XL");
        bigPastaCeci.addIngredient(pasta, 300); // 300g pasta
        bigPastaCeci.addIngredient(ceci, 100);  // 100g ceci

        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 200);  // solo 200g pasta -> NON basta
        pantry.addStock(ceci, 150);   // ceci ok

        MealPlan plan = new MealPlan(LocalDate.now());
        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
        plan.addDayPlan(monday);

        MealPlannerService service = new MealPlannerService(pantry);
        ShoppingList shoppingList = new ShoppingList();

        boolean assigned = service.assignRecipeUsingPantry(
                plan,
                DayOfWeek.MONDAY,
                MealType.LUNCH,
                bigPastaCeci,
                shoppingList
        );

        System.out.println("Recipe assigned? " + assigned);
        System.out.println();
        System.out.println("Current meal plan:");
        System.out.println(plan);
        System.out.println();
        System.out.println(shoppingList);
    }
}
