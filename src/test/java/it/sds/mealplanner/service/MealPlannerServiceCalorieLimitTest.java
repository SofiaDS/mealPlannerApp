package it.sds.mealplanner.service;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MealPlannerServiceCalorieLimitTest {

    @Test
    void autoAssignAnyRecipe_shouldNotAssignRecipeIfItExceedsDailyCalorieLimit() {

        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(400, 10, 80, 2));

        Recipe heavyPasta = Recipe.create("Heavy pasta", MealType.LUNCH);
        heavyPasta.addIngredient(pasta, 200); // 200g -> 400 * 2 = 800 kcal

        RecipeRepository recipeRepo = new InMemoryRecipeRepository();
        recipeRepo.save(heavyPasta);

        Pantry pantry = new Pantry();
        pantry.addStock(pasta, 1000);

        MealPlan plan = new MealPlan(LocalDate.now(), 500.0);
        plan.addDayPlan(new DayPlan(DayOfWeek.MONDAY));

        // --- Service con strategy di default (rotating) ---
        MealPlannerService service = new MealPlannerService(pantry, recipeRepo);

        boolean assigned = service.autoAssignAnyRecipe(plan, DayOfWeek.MONDAY, MealType.LUNCH);

        // Assert: non deve assegnare nulla perché 800 kcal > 500 limit
        assertFalse(assigned, "La ricetta non dovrebbe essere assegnata perché supera il limite kcal");

        DayPlan monday = plan.getDayPlan(DayOfWeek.MONDAY);
        assertNotNull(monday);

        MealSlot lunchSlot = monday.getMeals().stream()
                .filter(s -> s.getType() == MealType.LUNCH)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Slot LUNCH non trovato"));

        assertNull(lunchSlot.getRecipe(), "Lo slot LUNCH dovrebbe essere vuoto se il limite kcal è superato");
    }
}
