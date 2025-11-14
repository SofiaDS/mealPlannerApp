package it.sds.mealplanner.app;

import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.Unit;
import it.sds.mealplanner.model.Recipe;
//import it.sds.mealplanner.model.Pantry;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MealPlannerApp {

    public static void main(String[] args) {
        //create recipes
//        Recipe pastaCeci = Recipe.create("Pasta e ceci");
//        Recipe oatmeal = Recipe.create("Oatmeal proteico");
//        Recipe appleSnack = Recipe.create("Mela");
//        Recipe bananaSnack = Recipe.create("Banana");
//        Recipe orangeSnack = Recipe.create("Orange");
//        Recipe nutsSnack = Recipe.create("Frutta secca");
//        Recipe yogurtBreakfast = Recipe.create("Yogurt greco + frutta");
//        Recipe fishVeg = Recipe.create("Orata con patate e insalata");
//        Recipe cousVeg = Recipe.create("Cous cous con verdure di stagione");

//        monday.assignRecipe(MealType.BREAKFAST, oatmeal);
//        monday.assignRecipe(MealType.SNACK, appleSnack);
//        monday.assignRecipe(MealType.LUNCH, pastaCeci);
//        monday.assignRecipe(MealType.SNACK, nutsSnack);
//        monday.assignRecipe(MealType.DINNER, fishVeg);
//        monday.assignRecipe(MealType.SNACK, nutsSnack);
//        tuesday.assignRecipe(MealType.BREAKFAST, yogurtBreakfast);
//        tuesday.assignRecipe(MealType.SNACK, orangeSnack);
//        tuesday.assignRecipe(MealType.LUNCH, cousVeg);
//        tuesday.assignRecipe(MealType.SNACK, bananaSnack);
//        MealPlan plan = new MealPlan(LocalDate.now());
//
//        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
//        DayPlan tuesday = new DayPlan(DayOfWeek.TUESDAY);
//        plan.addDayPlan(monday);
//        plan.addDayPlan(tuesday);
//        plan.assignRecipeAuto(DayOfWeek.MONDAY,MealType.SNACK, appleSnack);
//        plan.assignRecipeAuto(DayOfWeek.MONDAY,MealType.SNACK, appleSnack);
//        plan.assignRecipeAuto(DayOfWeek.MONDAY,MealType.SNACK, appleSnack);
//
//        System.out.println(plan);

        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        Ingredient ceci = new Ingredient("Ceci", Unit.GRAM);

        Recipe pastaCeci = Recipe.create("Pasta e ceci");
        pastaCeci.addIngredient(pasta, 80);
        pastaCeci.addIngredient(ceci, 100);

        System.out.println(pastaCeci);

        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 200);  // 200g di pasta
        pantry.addStock(ceci, 50);    // 50g di ceci (NON basta per la ricetta)

        System.out.println("\nCan make pasta e ceci with current pantry? "
                + pantry.canMakeRecipe(pastaCeci)); // atteso: false
        pantry.addStock(ceci, 100); // ora 50 + 100 = 150g

        System.out.println("Can make pasta e ceci after adding ceci? "
                + pantry.canMakeRecipe(pastaCeci)); // atteso: true
        pantry.consumeForRecipe(pastaCeci);
        System.out.println("\nRemaining pasta in pantry: " + pantry.getAvailableQuantity(pasta));
        System.out.println("Remaining ceci in pantry: " + pantry.getAvailableQuantity(ceci));
    }
}
