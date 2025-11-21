package it.sds.mealplanner.app;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.nio.file.Path;
import java.io.IOException;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.service.MealPlannerService;
import it.sds.mealplanner.export.PlainTextExporter;
import it.sds.mealplanner.model.UserPreferences;
import it.sds.mealplanner.strategy.SmartRecipeSelectionStrategy;
import it.sds.mealplanner.model.DietaryTag;



public class MealPlannerApp {

    public static void main(String[] args) {

        // --- Ingredients ---
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));

        Ingredient rice = new Ingredient("Rice", Unit.GRAM);
        rice.setNutritionPerUnit(new NutritionFacts(130, 2.7, 28, 0.3));

        Ingredient couscous = new Ingredient("CousCous", Unit.GRAM);
        couscous.setNutritionPerUnit(new NutritionFacts(361, 13.7, 75, 1.1));

        Ingredient orata = new Ingredient("Orata", Unit.GRAM);
        orata.setNutritionPerUnit(new NutritionFacts(121, 20.7, 1,3.8));

        Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
        chicken.setNutritionPerUnit(new NutritionFacts(165, 30, 0, 3.5));

        Ingredient chickPeas = new Ingredient("Ceci", Unit.GRAM);
        chickPeas.setNutritionPerUnit(new NutritionFacts(364 ,21.9,63, 6));

        Ingredient redBeans = new Ingredient("Red beans", Unit.GRAM);
        redBeans.setNutritionPerUnit(new NutritionFacts(337,24.4,60,0.8));

        Ingredient egg = new Ingredient("Eggs", Unit.PIECE);
        egg.setNutritionPerUnit(new NutritionFacts(65,6.3,0.34,4.9));

        Ingredient lettuce = new Ingredient("Lettuce", Unit.GRAM);
        lettuce.setNutritionPerUnit(new NutritionFacts(21,1.8,2.97,0.4));

        Ingredient mushrooms = new Ingredient("Mushrooms", Unit.GRAM);
        mushrooms.setNutritionPerUnit(new NutritionFacts(25,4,4,1));

        Ingredient pumpkin = new Ingredient("Pumpkin", Unit.GRAM);
        pumpkin.setNutritionPerUnit(new NutritionFacts(26,1,3.5,0.1));

        Ingredient spinach = new Ingredient("Spinach", Unit.GRAM);
        spinach.setNutritionPerUnit(new NutritionFacts(23,2.8,3.6,0.4));

        Ingredient rucola = new Ingredient("Rucola", Unit.GRAM);
        rucola.setNutritionPerUnit(new NutritionFacts(26,2.9,3.6,0.7));

        Ingredient mela = new Ingredient("Mela", Unit.PIECE);
        mela.setNutritionPerUnit(new NutritionFacts(64,0.6,17,0.15));

        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        banana.setNutritionPerUnit(new NutritionFacts(110,1,28,9));

        Ingredient orange = new Ingredient("Orange", Unit.PIECE);
        orange.setNutritionPerUnit(new NutritionFacts(70,1.3,16.5,0.2));

        Ingredient pear = new Ingredient("Pear", Unit.PIECE);
        pear.setNutritionPerUnit(new NutritionFacts(96,0.6,26.7,0.2));

        Ingredient greekYNoFat = new Ingredient("Greek yogurt", Unit.GRAM);
        greekYNoFat.setNutritionPerUnit(new NutritionFacts(63,10,4,0.5));

        // --- Recipes ---
        // --- snacks ---
        Recipe appleSnack = Recipe.create("Apple snack", MealType.SNACK);
        appleSnack.addIngredient(mela, 1);
        appleSnack.addTag(DietaryTag.VEGAN);
        appleSnack.addTag(DietaryTag.VEGETARIAN);
        appleSnack.addTag(DietaryTag.LOW_FAT);

        Recipe orangeSnack = Recipe.create("Orange snack", MealType.SNACK);
        orangeSnack.addIngredient(orange, 1);
        orangeSnack.addTag(DietaryTag.VEGAN);
        orangeSnack.addTag(DietaryTag.VEGETARIAN);
        orangeSnack.addTag(DietaryTag.LOW_FAT);

        Recipe pearSnack = Recipe.create("Pear snack", MealType.SNACK);
        pearSnack.addIngredient(pear, 1);
        pearSnack.addTag(DietaryTag.VEGAN);
        pearSnack.addTag(DietaryTag.VEGETARIAN);
        pearSnack.addTag(DietaryTag.LOW_FAT);

        Recipe bananaSnack = Recipe.create("Banana snack", MealType.SNACK);
        bananaSnack.addIngredient(banana, 1);
        bananaSnack.addTag(DietaryTag.VEGAN);
        bananaSnack.addTag(DietaryTag.VEGETARIAN);
        bananaSnack.addTag(DietaryTag.LOW_FAT);
//       breakfast
        Recipe bananaY = Recipe.create("Greek yoghurt with banana", MealType.BREAKFAST);
        bananaY.addIngredient(greekYNoFat, 150);
        bananaY.addIngredient(banana, 1);
        bananaY.addTag(DietaryTag.VEGETARIAN);
        bananaY.addTag(DietaryTag.HIGH_PROTEIN);
        bananaY.addTag(DietaryTag.LOW_FAT); // yogurt no-fat

        Recipe appleY = Recipe.create("Fruit salad", MealType.BREAKFAST);
        appleY.addIngredient(mela, 2);
        appleY.addTag(DietaryTag.VEGAN);
        appleY.addTag(DietaryTag.VEGETARIAN);
        appleY.addTag(DietaryTag.LOW_FAT);
        //main
        Recipe pastaCeci = Recipe.create("Pasta e ceci", MealType.LUNCH);
        pastaCeci.addIngredient(pasta, 80);
        pastaCeci.addIngredient(chickPeas, 100);
        pastaCeci.addTag(DietaryTag.VEGAN);
        pastaCeci.addTag(DietaryTag.VEGETARIAN);
        pastaCeci.addTag(DietaryTag.HIGH_PROTEIN);

        Recipe grilledFish = Recipe.create("Grilled orata with rice", MealType.LUNCH);
        grilledFish.addIngredient(orata, 200);
        grilledFish.addIngredient(rice, 150);
        grilledFish.addTag(DietaryTag.PESCATARIAN);
        grilledFish.addTag(DietaryTag.HIGH_PROTEIN);
        grilledFish.addTag(DietaryTag.GLUTEN_FREE);

        Recipe chickenMushroom = Recipe.create("Chicken with mushrooms", MealType.DINNER);
        chickenMushroom.addIngredient(chicken, 250);
        chickenMushroom.addIngredient(mushrooms, 100);
        chickenMushroom.addTag(DietaryTag.HIGH_PROTEIN);
        chickenMushroom.addTag(DietaryTag.GLUTEN_FREE);

        Recipe cousVeg = Recipe.create("Cous cous with red beans, pumpkin & spinach", MealType.LUNCH);
        cousVeg.addIngredient(couscous, 60);
        cousVeg.addIngredient(redBeans, 80);
        cousVeg.addIngredient(pumpkin, 100);
        cousVeg.addIngredient(spinach, 100);
        cousVeg.addTag(DietaryTag.VEGAN);
        cousVeg.addTag(DietaryTag.VEGETARIAN);
        cousVeg.addTag(DietaryTag.HIGH_PROTEIN);

        Recipe coldSalad = Recipe.create("Salad with rucola, lettuce and eggs", MealType.DINNER);
        coldSalad.addIngredient(rucola, 150);
        coldSalad.addIngredient(lettuce, 150);
        coldSalad.addIngredient(egg, 2);
        coldSalad.addTag(DietaryTag.VEGETARIAN);
        coldSalad.addTag(DietaryTag.LOW_CARB);
        coldSalad.addTag(DietaryTag.HIGH_PROTEIN);

        Recipe appleYogurt = Recipe.create("Greek yoghurt with apple", MealType.BREAKFAST);
        appleYogurt.addIngredient(greekYNoFat, 150);
        appleYogurt.addIngredient(mela, 1);
        appleYogurt.addTag(DietaryTag.VEGETARIAN);
        appleYogurt.addTag(DietaryTag.HIGH_PROTEIN);
        appleYogurt.addTag(DietaryTag.LOW_FAT);

        Recipe chickpeaSalad = Recipe.create("Chickpeas salad with lettuce and rucola", MealType.LUNCH);
        chickpeaSalad.addIngredient(chickPeas, 120);
        chickpeaSalad.addIngredient(lettuce, 100);
        chickpeaSalad.addIngredient(rucola, 50);
        chickpeaSalad.addTag(DietaryTag.VEGAN);
        chickpeaSalad.addTag(DietaryTag.VEGETARIAN);
        chickpeaSalad.addTag(DietaryTag.HIGH_PROTEIN);
        chickpeaSalad.addTag(DietaryTag.LOW_FAT);

        Recipe eggsSpinach = Recipe.create("Eggs with spinach", MealType.DINNER);
        eggsSpinach.addIngredient(egg, 2);
        eggsSpinach.addIngredient(spinach, 100);
        eggsSpinach.addTag(DietaryTag.VEGETARIAN);
        eggsSpinach.addTag(DietaryTag.HIGH_PROTEIN);
        eggsSpinach.addTag(DietaryTag.LOW_CARB);


        // --- Recipe Repository ---
        RecipeRepository recipeRepo = new InMemoryRecipeRepository();
        recipeRepo.save(eggsSpinach);
        recipeRepo.save((appleYogurt));
        recipeRepo.save(chickpeaSalad);
        recipeRepo.save(pastaCeci);
        recipeRepo.save(appleSnack);
        recipeRepo.save(appleY);
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
        pantry.addStock(orata, 300);
        pantry.addStock(rice, 200);
        pantry.addStock(chicken, 300);
        pantry.addStock(lettuce, 100);
        pantry.addStock(mushrooms, 150);
        pantry.addStock(couscous,1000);
        pantry.addStock(greekYNoFat, 1000);
        pantry.addStock(banana,5);
        pantry.addStock(pear,1);

        // --- Meal plan for the whole week ---
        MealPlan plan = new MealPlan(LocalDate.now(), 2000.0);
        for (DayOfWeek day : DayOfWeek.values()) {
            plan.addDayPlan(new DayPlan(day));
        }

        // --- User preferences ---
        UserPreferences prefs = new UserPreferences();

        prefs.addPreferredTag(DietaryTag.VEGETARIAN);
        prefs.addPreferredTag(DietaryTag.HIGH_PROTEIN);

        prefs.addExcludedIngredient(orata);
        prefs.setMaxSameRecipePerWeek(2);
        prefs.setAvoidSameRecipeOnConsecutiveDays(true);

        // --- Service ---
        SmartRecipeSelectionStrategy strategy = new SmartRecipeSelectionStrategy(prefs);
        MealPlannerService service = new MealPlannerService(pantry, recipeRepo, strategy);
//        MealPlannerService service = new MealPlannerService(pantry, recipeRepo);

        for (DayOfWeek day : DayOfWeek.values()) {
            for (MealType type : MealType.values()) {
                service.autoAssignDayWithTwoSnacks(plan, day);
            }
        }

        System.out.println("===== MEAL PLAN =====");
        for (DayOfWeek day : DayOfWeek.values()) {
            DayPlan dayPlan = plan.getDayPlan(day);
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

        // Shopping list globale
        ShoppingList globalShoppingList = service.buildShoppingListForPlan(plan);

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

        PlainTextExporter exporter = new PlainTextExporter();
        DateTimeFormatter fileDateFmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileName = "mealplan_" + plan.getStartDate().format(fileDateFmt) + ".txt";
        Path outputFile = Path.of(fileName);

        try {
            exporter.export(plan, globalShoppingList, outputFile);
            System.out.println("\nFile esportato: " + outputFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Errore durante l'esportazione del piano: " + e.getMessage());
        }

//        System.out.println("DEBUG NUTRITION:");
//        System.out.println("  " + pastaCeci.getName() + " -> " +
//                pastaCeci.computeNutritionFacts().getCalories() + " kcal");
//        System.out.println("  " + bananaY.getName() + " -> " +
//                bananaY.computeNutritionFacts().getCalories() + " kcal");
    }
}
