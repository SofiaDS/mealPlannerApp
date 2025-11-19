package it.sds.mealplanner.app;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.service.MealPlannerService;

public class MealPlannerApp {

    public static void main(String[] args) {

        // --- Ingredients ---
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        Ingredient rice = new Ingredient("Rice", Unit.GRAM);
        Ingredient couscous = new Ingredient("CousCous", Unit.GRAM);

        Ingredient fish = new Ingredient("Fish", Unit.GRAM);
        Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
        Ingredient chickPeas = new Ingredient("Ceci", Unit.GRAM);
        Ingredient redBeans = new Ingredient("Red beans", Unit.GRAM);
        Ingredient egg = new Ingredient("Eggs", Unit.PIECE);

        Ingredient lettuce = new Ingredient("Lettuce", Unit.GRAM);
        Ingredient mushrooms = new Ingredient("Mushrooms", Unit.GRAM);
        Ingredient pumpkin = new Ingredient("Pumpkin", Unit.GRAM);
        Ingredient spinach = new Ingredient("Spinach", Unit.GRAM);
        Ingredient rucola = new Ingredient("Rucola", Unit.GRAM);

        Ingredient mela = new Ingredient("Mela", Unit.PIECE);
        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        Ingredient orange = new Ingredient("Orange", Unit.PIECE);
        Ingredient pear = new Ingredient("Pear", Unit.PIECE);
        Ingredient greekY = new Ingredient("Greek yogurt", Unit.GRAM);

        // --- Recipes ---
        // --- snacks ---
        Recipe appleSnack = Recipe.create("Apple snack", MealType.SNACK);
        appleSnack.addIngredient(mela, 1);

        Recipe orangeSnack = Recipe.create("Orange snack", MealType.SNACK);
        orangeSnack.addIngredient(orange, 1);

        Recipe pearSnack = Recipe.create("Pear snack", MealType.SNACK);
        pearSnack.addIngredient(pear, 1);

        Recipe bananaSnack = Recipe.create("Banana snack", MealType.SNACK);
        bananaSnack.addIngredient(banana, 1);

        Recipe bananaY = Recipe.create("Greek yoghurt and fruit", MealType.BREAKFAST);
        bananaY.addIngredient(greekY,150);
        bananaY.addIngredient(banana,1);

        Recipe fruitSalad = Recipe.create("Fruit salad", MealType.BREAKFAST);
        fruitSalad.addIngredient(mela, 2);
        fruitSalad.addIngredient(banana, 1);
        fruitSalad.addIngredient(lettuce, 50);
        // --- main ---
        Recipe pastaCeci = Recipe.create("Pasta e ceci", MealType.LUNCH);
        pastaCeci.addIngredient(pasta, 80);
        pastaCeci.addIngredient(chickPeas, 100);

        Recipe grilledFish = Recipe.create("Grilled fish with rice", MealType.LUNCH);
        grilledFish.addIngredient(fish, 200);
        grilledFish.addIngredient(rice, 150);

        Recipe chickenMushroom = Recipe.create("Chicken with mushrooms", MealType.DINNER);
        chickenMushroom.addIngredient(chicken, 250);
        chickenMushroom.addIngredient(mushrooms, 100);

        Recipe cousVeg = Recipe.create("Cous cous with vegetables", MealType.LUNCH);
        cousVeg.addIngredient(couscous, 60);
        cousVeg.addIngredient(redBeans, 80);
        cousVeg.addIngredient(pumpkin, 100);
        cousVeg.addIngredient(spinach,100);

        Recipe coldSalad = Recipe.create("Cold salad", MealType.DINNER);
        coldSalad.addIngredient(rucola,150);
        coldSalad.addIngredient(lettuce, 150);
        coldSalad.addIngredient(egg,2);


        // --- Recipe Repository ---
        RecipeRepository recipeRepo = new InMemoryRecipeRepository();
        recipeRepo.save(pastaCeci);
        recipeRepo.save(appleSnack);
        recipeRepo.save(fruitSalad);
        recipeRepo.save(grilledFish);
        recipeRepo.save(chickenMushroom);
        recipeRepo.save(coldSalad);
        recipeRepo.save(cousVeg);
        recipeRepo.save(bananaSnack);
        recipeRepo.save(orangeSnack);
        recipeRepo.save(pearSnack);
        recipeRepo.save(bananaY);

        // --- Pantry ---
        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 500);
        pantry.addStock(chickPeas, 150);
        pantry.addStock(mela, 2);
        pantry.addStock(fish, 300);
        pantry.addStock(rice, 200);
        pantry.addStock(chicken, 300);
        pantry.addStock(lettuce, 100);
        pantry.addStock(mushrooms, 150);
        pantry.addStock(couscous,1000);
        pantry.addStock(greekY, 1000);
        pantry.addStock(banana,5);
        pantry.addStock(pear,1);

        MealPlan weeklyPlan = new MealPlan(LocalDate.now());
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklyPlan.addDayPlan(new DayPlan(day));
        }

        MealPlannerService plannerService = new MealPlannerService(pantry, recipeRepo);
        for (DayOfWeek day : DayOfWeek.values()) {
            for (MealType type : MealType.values()) {
                boolean assigned = plannerService.autoAssignAnyRecipe(weeklyPlan, day, type);
                if (!assigned) {
                    System.out.println("Nessuna ricetta assegnata per " + day + " - " + type);
                }
            }
        }
        List<Recipe> allRecipes = recipeRepo.findAll();
        int index = 0;

        for (DayOfWeek day : DayOfWeek.values()) {
            for (MealType type : MealType.values()) {
                Recipe recipe = allRecipes.get(index % allRecipes.size());
                index++;

                boolean assigned = weeklyPlan.assignRecipeAuto(day, type, recipe);
                if (!assigned) {
                    System.out.println("Nessuna ricetta assegnata per " + day + " - " + type);
                }
            }
        }

        System.out.println("===== MEAL PLAN V3=====");
        for (DayOfWeek day : DayOfWeek.values()) {
            DayPlan dayPlan = weeklyPlan.getDayPlan(day);
            System.out.println("\n" + day + ":");
            if (dayPlan == null) {
                System.out.println("  (nessun pasto)");
                continue;
            }

            for (MealSlot slot : dayPlan.getMeals()) {
                MealType type = slot.getType();
                Recipe recipe = slot.getRecipe();
                String recipeName = (recipe != null) ? recipe.getName() : "(vuoto)";
                System.out.println("  " + type + " -> " + recipeName);
            }
        }

        ShoppingList globalShoppingList = plannerService.buildShoppingListForPlan(weeklyPlan);

        System.out.println("\n===== SHOPPING LIST GLOBALE =====");
        Map<Ingredient, Double> items = globalShoppingList.getItems();

        if (items.isEmpty()) {
            System.out.println("Nessun ingrediente da comprare: la pantry copre tutto il piano!");
        } else {
            for (Map.Entry<Ingredient, Double> entry : items.entrySet()) {
                Ingredient ing = entry.getKey();
                double qty = entry.getValue();
                System.out.println("- " + ing.getName() + ": " + qty + " " + ing.getUnit());
            }
        }
    }
}
