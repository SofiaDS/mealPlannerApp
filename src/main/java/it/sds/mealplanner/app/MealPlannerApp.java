package it.sds.mealplanner.app;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import it.sds.mealplanner.export.PlainTextExporter;
import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.DietaryTag;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.NutritionFacts;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.ShoppingList;
import it.sds.mealplanner.model.Unit;
import it.sds.mealplanner.model.UserPreferences;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.service.MealPlannerService;
import it.sds.mealplanner.strategy.SmartRecipeSelectionStrategy;

public class MealPlannerApp {

    public static void main(String[] args) {

        // --- Ingredients ---
        // cereals
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));
        Ingredient rice = new Ingredient("Rice", Unit.GRAM);
        rice.setNutritionPerUnit(new NutritionFacts(130, 2.7, 28, 0.3));
        Ingredient couscous = new Ingredient("CousCous", Unit.GRAM);
        couscous.setNutritionPerUnit(new NutritionFacts(361, 13.7, 75, 1.1));
        Ingredient bread = new Ingredient("Bread", Unit.GRAM);
        bread.setNutritionPerUnit(new NutritionFacts(265, 9, 49, 3.2));
        Ingredient potato = new Ingredient("Potato", Unit.GRAM);
        potato.setNutritionPerUnit(new NutritionFacts(77, 2, 17, 0.1));
        Ingredient cornFlakes = new Ingredient("Corn flakes", Unit.GRAM);
        cornFlakes.setNutritionPerUnit(new NutritionFacts(357, 8, 84.5, 1));
        Ingredient oats = new Ingredient("Oats", Unit.GRAM);
        oats.setNutritionPerUnit(new NutritionFacts(389, 16.9, 66.3, 6.9));
        Ingredient barley = new Ingredient("Barley", Unit.GRAM);
        barley.setNutritionPerUnit(new NutritionFacts(354, 12.5, 73.5, 2.3));

        // animal proteins
        Ingredient orata = new Ingredient("Orata", Unit.GRAM);
        orata.setNutritionPerUnit(new NutritionFacts(121, 20.7, 1, 3.8));
        Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
        chicken.setNutritionPerUnit(new NutritionFacts(165, 30, 0, 3.5));
        Ingredient salmon = new Ingredient("Salmon", Unit.GRAM);
        salmon.setNutritionPerUnit(new NutritionFacts(208, 20, 0, 13));
        Ingredient tuna = new Ingredient("Tuna", Unit.GRAM);
        tuna.setNutritionPerUnit(new NutritionFacts(132, 28, 0, 1.3));
        Ingredient beef = new Ingredient("Beef", Unit.GRAM);
        beef.setNutritionPerUnit(new NutritionFacts(250, 26, 0, 15));
        Ingredient pork = new Ingredient("Pork", Unit.GRAM);
        pork.setNutritionPerUnit(new NutritionFacts(242, 27, 0, 14));
        Ingredient egg = new Ingredient("Eggs", Unit.PIECE);
        egg.setNutritionPerUnit(new NutritionFacts(65, 6.3, 0.34, 4.9));

        // plant proteins
        Ingredient chickPeas = new Ingredient("Ceci", Unit.GRAM);
        chickPeas.setNutritionPerUnit(new NutritionFacts(364, 21.9, 63, 6));
        Ingredient redBeans = new Ingredient("Red beans", Unit.GRAM);
        redBeans.setNutritionPerUnit(new NutritionFacts(337, 24.4, 60, 0.8));
        Ingredient lentils = new Ingredient("Lentils", Unit.GRAM);
        lentils.setNutritionPerUnit(new NutritionFacts(353, 25.8, 60, 1.1));
        Ingredient tofu = new Ingredient("Tofu", Unit.GRAM);
        tofu.setNutritionPerUnit(new NutritionFacts(76, 8, 1.9, 4.8));
        Ingredient tempeh = new Ingredient("Tempeh", Unit.GRAM);
        tempeh.setNutritionPerUnit(new NutritionFacts(193, 20.3, 7.6, 10.8));
        Ingredient seitan = new Ingredient("Seitan", Unit.GRAM);
        seitan.setNutritionPerUnit(new NutritionFacts(121, 21, 4, 2));

        // vegetables
        Ingredient lettuce = new Ingredient("Lettuce", Unit.GRAM);
        lettuce.setNutritionPerUnit(new NutritionFacts(21, 1.8, 2.97, 0.4));
        Ingredient mushrooms = new Ingredient("Mushrooms", Unit.GRAM);
        mushrooms.setNutritionPerUnit(new NutritionFacts(25, 4, 4, 1));
        Ingredient pumpkin = new Ingredient("Pumpkin", Unit.GRAM);
        pumpkin.setNutritionPerUnit(new NutritionFacts(26, 1, 3.5, 0.1));
        Ingredient spinach = new Ingredient("Spinach", Unit.GRAM);
        spinach.setNutritionPerUnit(new NutritionFacts(23, 2.8, 3.6, 0.4));
        Ingredient rucola = new Ingredient("Rucola", Unit.GRAM);
        rucola.setNutritionPerUnit(new NutritionFacts(26, 2.9, 3.6, 0.7));
        Ingredient tomato = new Ingredient("Tomato", Unit.GRAM);
        tomato.setNutritionPerUnit(new NutritionFacts(18, 0.9, 3.9, 0.2));
        Ingredient carrot = new Ingredient("Carrot", Unit.GRAM);
        carrot.setNutritionPerUnit(new NutritionFacts(41, 0.9, 10, 0.2));
        Ingredient cucumber = new Ingredient("Cucumber", Unit.GRAM);
        cucumber.setNutritionPerUnit(new NutritionFacts(16, 0.7, 3.6, 0.1));
        Ingredient bellPepper = new Ingredient("Bell pepper", Unit.GRAM);
        bellPepper.setNutritionPerUnit(new NutritionFacts(31, 1, 6, 0.3));
        Ingredient onion = new Ingredient("Onion", Unit.GRAM);
        onion.setNutritionPerUnit(new NutritionFacts(40, 1.1, 9.3, 0.1));
        Ingredient celery = new Ingredient("Celery", Unit.GRAM);
        celery.setNutritionPerUnit(new NutritionFacts(16, 0.7, 3, 0.2));
        Ingredient zucchini = new Ingredient("Zucchini", Unit.GRAM);
        zucchini.setNutritionPerUnit(new NutritionFacts(17, 1.2, 3.1, 0.3));

        // fruits
        Ingredient apple = new Ingredient("Mela", Unit.PIECE);
        apple.setNutritionPerUnit(new NutritionFacts(64, 0.6, 17, 0.15));
        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        banana.setNutritionPerUnit(new NutritionFacts(110, 1, 28, 9));
        Ingredient orange = new Ingredient("Orange", Unit.PIECE);
        orange.setNutritionPerUnit(new NutritionFacts(70, 1.3, 16.5, 0.2));
        Ingredient pear = new Ingredient("Pear", Unit.PIECE);
        pear.setNutritionPerUnit(new NutritionFacts(96, 0.6, 26.7, 0.2));
        Ingredient strawberry = new Ingredient("Strawberry", Unit.GRAM);
        strawberry.setNutritionPerUnit(new NutritionFacts(32, 0.7, 7.7, 0.3));
        Ingredient blueberry = new Ingredient("Blueberry", Unit.GRAM);
        blueberry.setNutritionPerUnit(new NutritionFacts(57, 0.7, 14, 0.3));
        Ingredient raspberry = new Ingredient("Raspberry", Unit.GRAM);
        raspberry.setNutritionPerUnit(new NutritionFacts(52, 1.2, 12, 0.7));
        Ingredient kiwi = new Ingredient("Kiwi", Unit.PIECE);
        kiwi.setNutritionPerUnit(new NutritionFacts(42, 0.8, 10, 0.4));
        Ingredient pineapple = new Ingredient("Pineapple", Unit.GRAM);
        pineapple.setNutritionPerUnit(new NutritionFacts(50, 0.5, 13, 0.1));
        Ingredient mango = new Ingredient("Mango", Unit.GRAM);
        mango.setNutritionPerUnit(new NutritionFacts(60, 0.8, 15, 0.4));
        Ingredient grapes = new Ingredient("Grapes", Unit.GRAM);
        grapes.setNutritionPerUnit(new NutritionFacts(69, 0.7, 18, 0.2));

        // dairy
        Ingredient greekYNoFat = new Ingredient("Greek yogurt", Unit.GRAM);
        greekYNoFat.setNutritionPerUnit(new NutritionFacts(63, 10, 4, 0.5));
        Ingredient fullFatMilk = new Ingredient("Full-fat milk", Unit.GRAM);
        fullFatMilk.setNutritionPerUnit(new NutritionFacts(61, 3.2, 5, 3.3));
        Ingredient oatMilk = new Ingredient("Oat milk", Unit.GRAM);
        oatMilk.setNutritionPerUnit(new NutritionFacts(47, 1, 6, 1.5));
        Ingredient mozzarella = new Ingredient("Mozzarella", Unit.GRAM);
        mozzarella.setNutritionPerUnit(new NutritionFacts(280, 28, 3, 17));
        Ingredient cheddarCheese = new Ingredient("Cheddar cheese", Unit.GRAM);
        cheddarCheese.setNutritionPerUnit(new NutritionFacts(403, 25, 1.3, 33));
        Ingredient fetaCheese = new Ingredient("Feta cheese", Unit.GRAM);
        fetaCheese.setNutritionPerUnit(new NutritionFacts(264, 14, 4, 21));

        // --- Recipes ---
        // --- snacks ---
        Recipe appleSnack = Recipe.create("Apple snack", MealType.SNACK);
        appleSnack.addIngredient(apple, 1);
        appleSnack.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe orangeSnack = Recipe.create("Orange snack", MealType.SNACK);
        orangeSnack.addIngredient(orange, 1);
        orangeSnack.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe pearSnack = Recipe.create("Pear snack", MealType.SNACK);
        pearSnack.addIngredient(pear, 1);
        pearSnack.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe bananaSnack = Recipe.create("Banana snack", MealType.SNACK);
        bananaSnack.addIngredient(banana, 1);
        bananaSnack.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe fruitMixSnack1 = Recipe.create("Fruit mix snack apple, banana, orange", MealType.SNACK);
        fruitMixSnack1.addIngredient(apple, 1);
        fruitMixSnack1.addIngredient(banana, 1);
        fruitMixSnack1.addIngredient(orange, 1);
        fruitMixSnack1.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe fruitMixSnack2 = Recipe.create("Fruit mix snack 2: pear, kiwi", MealType.SNACK);
        fruitMixSnack2.addIngredient(pear, 1);
        fruitMixSnack2.addIngredient(kiwi, 1);
        fruitMixSnack2.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe fruitMixSnack3 = Recipe.create("Fruit mix snack 3: mango, pineapple, strawberry", MealType.SNACK);
        fruitMixSnack3.addIngredient(mango, 1);
        fruitMixSnack3.addIngredient(pineapple, 100);
        fruitMixSnack3.addIngredient(strawberry, 100);
        fruitMixSnack3.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        // breakfast
        Recipe bananaY = Recipe.create("Greek yoghurt with banana", MealType.BREAKFAST);
        bananaY.addIngredient(greekYNoFat, 150);
        bananaY.addIngredient(banana, 1);
        bananaY.addTags(java.util.Arrays.asList(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT)); // yogurt
                                                                                                                      // no-fat

        Recipe appleY = Recipe.create("Fruit salad", MealType.BREAKFAST);
        appleY.addIngredient(apple, 2);
        appleY.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.LOW_FAT));

        Recipe oatMilkCereal = Recipe.create("Oat milk with corn flakes", MealType.BREAKFAST);
        oatMilkCereal.addIngredient(oatMilk, 200);
        oatMilkCereal.addIngredient(cornFlakes, 50);
        oatMilkCereal.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN));

        Recipe yogurtFruitMix = Recipe.create("Greek yoghurt with mixed red fruits", MealType.BREAKFAST);
        yogurtFruitMix.addIngredient(greekYNoFat, 150);
        yogurtFruitMix.addIngredient(strawberry, 50);
        yogurtFruitMix.addIngredient(blueberry, 50);
        yogurtFruitMix.addIngredient(raspberry, 50);
        yogurtFruitMix
                .addTags(java.util.Arrays.asList(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT));

        Recipe bananaOats = Recipe.create("Banana with oats", MealType.BREAKFAST);
        bananaOats.addIngredient(banana, 1);
        bananaOats.addIngredient(oats, 50);
        bananaOats.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN));

        Recipe appleYogurt = Recipe.create("Greek yoghurt with apple", MealType.BREAKFAST);
        appleYogurt.addIngredient(greekYNoFat, 150);
        appleYogurt.addIngredient(apple, 1);
        appleYogurt
                .addTags(java.util.Arrays.asList(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT));

        // main
        Recipe pastaCeci = Recipe.create("Pasta e ceci", MealType.LUNCH);
        pastaCeci.addIngredient(pasta, 80);
        pastaCeci.addIngredient(chickPeas, 100);
        pastaCeci.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));

        Recipe grilledFish = Recipe.create("Grilled orata with rice", MealType.LUNCH);
        grilledFish.addIngredient(orata, 200);
        grilledFish.addIngredient(rice, 150);
        grilledFish.addTags(
                java.util.Arrays.asList(DietaryTag.PESCATARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));

        Recipe chickenMushroom = Recipe.create("Chicken with mushrooms", MealType.DINNER);
        chickenMushroom.addIngredient(chicken, 250);
        chickenMushroom.addIngredient(mushrooms, 100);
        chickenMushroom.addTags(java.util.Arrays.asList(DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));

        Recipe cousVeg = Recipe.create("Cous cous with red beans, pumpkin & spinach", MealType.LUNCH);
        cousVeg.addIngredient(couscous, 60);
        cousVeg.addIngredient(redBeans, 80);
        cousVeg.addIngredient(pumpkin, 100);
        cousVeg.addIngredient(spinach, 100);
        cousVeg.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));

        Recipe coldSalad = Recipe.create("Salad with rucola, lettuce and eggs", MealType.DINNER);
        coldSalad.addIngredient(rucola, 150);
        coldSalad.addIngredient(lettuce, 150);
        coldSalad.addIngredient(egg, 2);
        coldSalad.addTags(java.util.Arrays.asList(DietaryTag.VEGETARIAN, DietaryTag.LOW_CARB, DietaryTag.HIGH_PROTEIN));

        Recipe chickpeaSalad = Recipe.create("Chickpeas salad with lettuce and rucola", MealType.LUNCH);
        chickpeaSalad.addIngredient(chickPeas, 120);
        chickpeaSalad.addIngredient(lettuce, 100);
        chickpeaSalad.addIngredient(rucola, 50);
        chickpeaSalad.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN,
                DietaryTag.LOW_FAT));

        Recipe eggsSpinach = Recipe.create("Eggs with spinach", MealType.DINNER);
        eggsSpinach.addIngredient(egg, 2);
        eggsSpinach.addIngredient(spinach, 100);
        eggsSpinach
                .addTags(java.util.Arrays.asList(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_CARB));

        Recipe pastaTomato = Recipe.create("Pasta with tomato sauce", MealType.LUNCH);
        pastaTomato.addIngredient(pasta, 80);
        pastaTomato.addIngredient(tomato, 150);
        pastaTomato.addTags(java.util.Arrays.asList(DietaryTag.LOW_FAT, DietaryTag.VEGAN, DietaryTag.VEGETARIAN));

        Recipe tofuVeggies = Recipe.create("Tofu with peppers & zucchuni", MealType.DINNER);
        tofuVeggies.addIngredient(tofu, 150);
        tofuVeggies.addIngredient(bellPepper, 100);
        tofuVeggies.addIngredient(zucchini, 100);
        tofuVeggies.addTags(java.util.Arrays.asList(DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT, DietaryTag.VEGAN,
                DietaryTag.VEGETARIAN));

        Recipe lentilSoup = Recipe.create("Lentil soup with carrots & celery", MealType.LUNCH);
        lentilSoup.addIngredient(lentils, 100);
        lentilSoup.addIngredient(carrot, 100);
        lentilSoup.addIngredient(celery, 100);
        lentilSoup.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN,
                DietaryTag.GLUTEN_FREE));

        Recipe beefPotato = Recipe.create("Beef with roasted potatoes", MealType.DINNER);
        beefPotato.addIngredient(beef, 200);
        beefPotato.addIngredient(potato, 150);
        beefPotato.addTag(DietaryTag.HIGH_PROTEIN);

        Recipe porkBarley = Recipe.create("Pork with barley salad", MealType.LUNCH);
        porkBarley.addIngredient(pork, 200);
        porkBarley.addIngredient(barley, 100);

        Recipe salmonRice = Recipe.create("Salmon with rice and spinach", MealType.DINNER);
        salmonRice.addIngredient(salmon, 200);
        salmonRice.addIngredient(rice, 150);
        salmonRice.addIngredient(spinach, 100);
        salmonRice.addTags(java.util.Arrays.asList(DietaryTag.PESCATARIAN, DietaryTag.HIGH_PROTEIN));

        Recipe tunaSalad = Recipe.create("Tuna salad with lettuce and tomato", MealType.LUNCH);
        tunaSalad.addIngredient(tuna, 150);
        tunaSalad.addIngredient(lettuce, 100);
        tunaSalad.addIngredient(tomato, 100);
        tunaSalad
                .addTags(java.util.Arrays.asList(DietaryTag.LOW_CARB, DietaryTag.HIGH_PROTEIN, DietaryTag.PESCATARIAN));

        Recipe tempehVeggies = Recipe.create("Tempeh with pepper & zucchini", MealType.DINNER);
        tempehVeggies.addIngredient(tempeh, 150);
        tempehVeggies.addIngredient(bellPepper, 100);
        tempehVeggies.addIngredient(zucchini, 100);
        tempehVeggies
                .addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));

        Recipe seitanMushroom = Recipe.create("Seitan with mushrooms", MealType.DINNER);
        seitanMushroom.addIngredient(seitan, 150);
        seitanMushroom.addIngredient(mushrooms, 100);
        seitanMushroom
                .addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));

        Recipe tofuSalad = Recipe.create("Tofu salad with lettuce and rucola", MealType.LUNCH);
        tofuSalad.addIngredient(tofu, 150);
        tofuSalad.addIngredient(lettuce, 100);
        tofuSalad.addIngredient(rucola, 50);
        tofuSalad.addTags(java.util.Arrays.asList(DietaryTag.VEGAN, DietaryTag.LOW_CARB, DietaryTag.VEGETARIAN,
                DietaryTag.LOW_FAT));

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
        recipeRepo.save(oatMilkCereal);
        recipeRepo.save(yogurtFruitMix);
        recipeRepo.save(fruitMixSnack1);
        recipeRepo.save(fruitMixSnack2);
        recipeRepo.save(fruitMixSnack3);
        recipeRepo.save(pastaTomato);
        recipeRepo.save(tofuVeggies);
        recipeRepo.save(lentilSoup);
        recipeRepo.save(beefPotato);
        recipeRepo.save(porkBarley);
        recipeRepo.save(salmonRice);
        recipeRepo.save(tunaSalad);
        recipeRepo.save(tempehVeggies);
        recipeRepo.save(seitanMushroom);
        recipeRepo.save(tofuSalad);

        // --- Pantry ---
        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 500);
        pantry.addStock(chickPeas, 150);
        pantry.addStock(apple, 2);
        pantry.addStock(orata, 300);
        pantry.addStock(rice, 200);
        pantry.addStock(chicken, 300);
        pantry.addStock(lettuce, 100);
        pantry.addStock(mushrooms, 150);
        pantry.addStock(couscous, 1000);
        pantry.addStock(greekYNoFat, 1000);
        pantry.addStock(banana, 5);
        pantry.addStock(pear, 1);

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
        // MealPlannerService service = new MealPlannerService(pantry, recipeRepo);

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

        // System.out.println("DEBUG NUTRITION:");
        // System.out.println(" " + pastaCeci.getName() + " -> " +
        // pastaCeci.computeNutritionFacts().getCalories() + " kcal");
        // System.out.println(" " + bananaY.getName() + " -> " +
        // bananaY.computeNutritionFacts().getCalories() + " kcal");
    }
}
