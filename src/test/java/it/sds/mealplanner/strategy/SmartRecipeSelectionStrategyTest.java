package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmartRecipeSelectionStrategyTest {

    @Test
    void selectRecipe_shouldPreferRecipeMatchingPreferredTags() {

        Ingredient greekY = new Ingredient("Greek yogurt", Unit.GRAM);
        greekY.setNutritionPerUnit(new NutritionFacts(60, 10, 4, 0.2));

        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        banana.setNutritionPerUnit(new NutritionFacts(110, 1, 28, 0.9));

        Recipe vegHighProtein = Recipe.create("Greek yoghurt with banana", MealType.BREAKFAST);
        vegHighProtein.addIngredient(greekY, 150);
        vegHighProtein.addIngredient(banana, 1);
        vegHighProtein.addTag(DietaryTag.VEGETARIAN);
        vegHighProtein.addTag(DietaryTag.HIGH_PROTEIN);

        Recipe genericBreakfast = Recipe.create("Generic breakfast", MealType.BREAKFAST);
        genericBreakfast.addIngredient(banana, 1);

        UserPreferences prefs = new UserPreferences();
        prefs.addPreferredTag(DietaryTag.VEGETARIAN);
        prefs.addPreferredTag(DietaryTag.HIGH_PROTEIN);

        SmartRecipeSelectionStrategy strategy = new SmartRecipeSelectionStrategy(prefs);

        MealPlan plan = new MealPlan(LocalDate.now(), null);
        plan.addDayPlan(new DayPlan(DayOfWeek.MONDAY));

        Pantry pantry = new Pantry();
        pantry.addStock(greekY, 500);
        pantry.addStock(banana, 5);

        Recipe chosen = strategy.selectRecipe(
                List.of(vegHighProtein, genericBreakfast),
                plan,
                DayOfWeek.MONDAY,
                MealType.BREAKFAST,
                pantry
        );

        assertEquals(vegHighProtein, chosen,
                "La strategy dovrebbe preferire la ricetta con tag VEGETARIAN + HIGH_PROTEIN");
    }

    @Test
    void selectRecipe_shouldAvoidRecipesWithExcludedIngredient() {

        Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
        chicken.setNutritionPerUnit(new NutritionFacts(165, 30, 0, 3.5));

        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));

        Recipe chickenDish = Recipe.create("Chicken with something", MealType.DINNER);
        chickenDish.addIngredient(chicken, 200);

        Recipe pastaDish = Recipe.create("Simple pasta", MealType.DINNER);
        pastaDish.addIngredient(pasta, 80);

        UserPreferences prefs = new UserPreferences();
        prefs.addExcludedIngredient(chicken);

        SmartRecipeSelectionStrategy strategy = new SmartRecipeSelectionStrategy(prefs);

        MealPlan plan = new MealPlan(LocalDate.now(), null);
        plan.addDayPlan(new DayPlan(DayOfWeek.MONDAY));

        Pantry pantry = new Pantry();
        pantry.addStock(chicken, 500);
        pantry.addStock(pasta, 500);

        Recipe chosen = strategy.selectRecipe(
                List.of(chickenDish, pastaDish),
                plan,
                DayOfWeek.MONDAY,
                MealType.DINNER,
                pantry
        );

        assertEquals(pastaDish, chosen,
                "La strategy dovrebbe scartare le ricette con ingredienti esclusi");
    }
}
