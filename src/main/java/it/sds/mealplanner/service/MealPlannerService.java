package it.sds.mealplanner.service;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.strategy.RecipeSelectionStrategy;
import it.sds.mealplanner.strategy.RotatingRecipeSelectionStrategy;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;

public class MealPlannerService {

    private final Pantry pantry;
    private final RecipeRepository recipeRepository;
    private final RecipeSelectionStrategy recipeSelectionStrategy;

    public MealPlannerService(Pantry pantry,
                              RecipeRepository recipeRepository,
                              RecipeSelectionStrategy recipeSelectionStrategy) {
        if (pantry == null) {
            throw new IllegalArgumentException("Pantry cannot be null");
        }
        if (recipeRepository == null) {
            throw new IllegalArgumentException("RecipeRepository cannot be null");
        }
        if (recipeSelectionStrategy == null) {
            throw new IllegalArgumentException("RecipeSelectionStrategy cannot be null");
        }
        this.pantry = pantry;
        this.recipeRepository = recipeRepository;
        this.recipeSelectionStrategy = recipeSelectionStrategy;
    }

    public MealPlannerService(Pantry pantry, RecipeRepository recipeRepository) {
        this(pantry, recipeRepository, new RotatingRecipeSelectionStrategy());
    }

    public boolean assignRecipeUsingPantry(MealPlan plan,
                                           DayOfWeek startDay,
                                           MealType type,
                                           Recipe recipe,
                                           ShoppingList shoppingList) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        if (shoppingList == null) {
            throw new IllegalArgumentException("ShoppingList cannot be null");
        }

        Map<Ingredient, Double> missing = pantry.calculateMissingForRecipe(recipe);

        if (!missing.isEmpty()) {
            shoppingList.addAll(missing);
        }

        plan.assignRecipeAuto(startDay, type, recipe);

        return missing.isEmpty();
    }

    public boolean assignRecipeUsingPantry(MealPlan plan,
                                           DayOfWeek startDay,
                                           MealType type,
                                           Recipe recipe) {
        return assignRecipeUsingPantry(plan, startDay, type, recipe, new ShoppingList());
    }

    /**
     * Restituisce la prima ricetta disponibile nel repository.
     * Nota: in passato filtrava in base alla Pantry (solo ricette "cookable"),
     * ma con il nuovo modello di pianificazione il MealPlan non dipende più dalla dispensa.
     * Per questo motivo non applichiamo più il filtro sulla Pantry.
     */
    @Deprecated
    public Optional<Recipe> findFirstCookableRecipe() {
        return recipeRepository.findAll()
                .stream()
                .findFirst();
    }

    public boolean autoAssignAnyRecipe(MealPlan plan,
                                       DayOfWeek startDay,
                                       MealType type) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }

        var recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            return false;
        }

        Recipe recipe = recipeSelectionStrategy.selectRecipe(
                recipes,
                plan,
                startDay,
                type,
                pantry
        );

        if (recipe == null) {
            return false;
        }
        return plan.assignRecipeAuto(startDay, type, recipe);
    }

    public ShoppingList buildShoppingListForPlan(MealPlan plan, Pantry pantry) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (pantry == null) {
            throw new IllegalArgumentException("Pantry cannot be null");
        }

        Map<Ingredient, Double> totalRequired = new java.util.HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            DayPlan dayPlan = plan.getDayPlan(day);
            if (dayPlan == null) {
                continue;
            }

            for (MealSlot slot : dayPlan.getMeals()) {
                Recipe recipe = slot.getRecipe();
                if (recipe == null) {
                    continue;
                }

                for (RecipeIngredient ri : recipe.getIngredients()) {
                    Ingredient ing = ri.getIngredient();
                    double qty = ri.getQuantityRequired();
                    totalRequired.merge(ing, qty, Double::sum);
                }
            }
        }

       ShoppingList shoppingList = new ShoppingList();

        for (Map.Entry<Ingredient, Double> entry : totalRequired.entrySet()) {
            Ingredient ing = entry.getKey();
            double required = entry.getValue();
            double available = pantry.getAvailableQuantity(ing);

            if (required > available) {
                double toBuy = required - available;
                shoppingList.addItem(ing, toBuy);
            }
        }

        return shoppingList;
    }

    public ShoppingList buildShoppingListForPlan(MealPlan plan) {
        return buildShoppingListForPlan(plan, this.pantry);
    }

}
