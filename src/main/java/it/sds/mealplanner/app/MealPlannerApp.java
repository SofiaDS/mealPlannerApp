package it.sds.mealplanner.app;

import java.time.DayOfWeek;
import java.time.LocalDate;

import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.ShoppingList;
import it.sds.mealplanner.model.Unit;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.service.MealPlannerService;

public class MealPlannerApp {

    public static void main(String[] args) {

        // --- Ingredients ---
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        Ingredient ceci = new Ingredient("Ceci", Unit.GRAM);
        Ingredient mela = new Ingredient("Mela", Unit.PIECE);
        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        Ingredient fish = new Ingredient("Fish", Unit.GRAM);
        Ingredient rice = new Ingredient("Rice", Unit.GRAM);
        Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
        Ingredient lettuce = new Ingredient("Lettuce", Unit.GRAM);
        Ingredient mushrooms = new Ingredient("Mushrooms", Unit.GRAM);

        // --- Recipes ---
        Recipe pastaCeci = Recipe.create("Pasta e ceci");
        pastaCeci.addIngredient(pasta, 80);
        pastaCeci.addIngredient(ceci, 100);

        Recipe appleSnack = Recipe.create("Apple snack");
        appleSnack.addIngredient(mela, 1);

        Recipe fruitSalad = Recipe.create("Fruit salad");
        fruitSalad.addIngredient(mela, 2);
        fruitSalad.addIngredient(banana, 1);
        fruitSalad.addIngredient(lettuce, 50);

        Recipe grilledFish = Recipe.create("Grilled fish with rice");
        grilledFish.addIngredient(fish, 200);
        grilledFish.addIngredient(rice, 150);

        Recipe chickenMushroom = Recipe.create("Chicken with mushrooms");
        chickenMushroom.addIngredient(chicken, 250);
        chickenMushroom.addIngredient(mushrooms, 100);

        // --- Recipe Repository ---
        RecipeRepository recipeRepo = new InMemoryRecipeRepository();
        recipeRepo.save(pastaCeci);
        recipeRepo.save(appleSnack);
        recipeRepo.save(fruitSalad);
        recipeRepo.save(grilledFish);
        recipeRepo.save(chickenMushroom);

        // --- Pantry ---
        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 500);
        pantry.addStock(ceci, 150);
        pantry.addStock(mela, 2);
        pantry.addStock(fish, 300);
        pantry.addStock(rice, 200);
        pantry.addStock(chicken, 300);
        pantry.addStock(lettuce, 100);
        pantry.addStock(mushrooms, 150);

        // --- Meal plan with two days ---
        MealPlan plan = new MealPlan(LocalDate.now());
        DayPlan monday = new DayPlan(DayOfWeek.MONDAY);
        DayPlan tuesday = new DayPlan(DayOfWeek.TUESDAY);
        DayPlan wednesday = new DayPlan(DayOfWeek.WEDNESDAY);
        DayPlan thursday = new DayPlan(DayOfWeek.THURSDAY);
        DayPlan friday = new DayPlan(DayOfWeek.FRIDAY);
        DayPlan saturday = new DayPlan(DayOfWeek.SATURDAY);
        DayPlan sunday = new DayPlan(DayOfWeek.SUNDAY);

        plan.addDayPlan(monday);
        plan.addDayPlan(tuesday);
        plan.addDayPlan(wednesday);
        plan.addDayPlan(thursday);
        plan.addDayPlan(friday);
        plan.addDayPlan(saturday);
        plan.addDayPlan(sunday);

        // --- Service ---
        MealPlannerService service = new MealPlannerService(pantry, recipeRepo);

        // --- Shopping list ---
        ShoppingList shoppingList = new ShoppingList();

        // 1) Proviamo l'autoAssign: sceglie la prima ricetta cucinabile dal repo
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.MONDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.MONDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.MONDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.MONDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.MONDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.TUESDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.TUESDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.TUESDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.TUESDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.TUESDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.WEDNESDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.WEDNESDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.WEDNESDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.WEDNESDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.WEDNESDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.THURSDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.THURSDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.THURSDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.THURSDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.THURSDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.FRIDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.FRIDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.FRIDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.FRIDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.FRIDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SATURDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SATURDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SATURDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SATURDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SATURDAY, MealType.BREAKFAST);

        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SUNDAY, MealType.LUNCH);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SUNDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SUNDAY, MealType.SNACK);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SUNDAY, MealType.DINNER);
        service.autoAssignFirstCookableRecipe(plan, DayOfWeek.SUNDAY, MealType.BREAKFAST);

        // 2) case not enough ingredients
        Recipe bigPasta = Recipe.create("Pasta gigante");
        bigPasta.addIngredient(pasta, 1000); // molto più della pasta disponibile

        boolean assigned = service.assignRecipeUsingPantry(
                plan,
                DayOfWeek.MONDAY,
                MealType.DINNER,
                bigPasta,
                shoppingList);

        System.out.println("Big pasta assigned? " + assigned);
        System.out.println("\nMeal plan:");
        System.out.println(plan);
        System.out.println("\nShopping list:");
        System.out.println(shoppingList);

        System.out.println("Remaining pasta: " + pantry.getAvailableQuantity(pasta));
        System.out.println("Remaining ceci: " + pantry.getAvailableQuantity(ceci));
        System.out.println("Remaining mele: " + pantry.getAvailableQuantity(mela));
    }
}
