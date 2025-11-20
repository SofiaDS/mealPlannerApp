package it.sds.mealplanner.service;

import it.sds.mealplanner.model.*;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.strategy.RecipeSelectionStrategy;
import it.sds.mealplanner.strategy.RotatingRecipeSelectionStrategy;

import java.time.DayOfWeek;
import java.util.HashMap;
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
            return false;
        }

        pantry.consumeForRecipe(recipe);
        return assignToPlan(plan, startDay, type, recipe);
    }

    public boolean assignRecipeUsingPantry(MealPlan plan,
                                           DayOfWeek startDay,
                                           MealType type,
                                           Recipe recipe) {
        return assignRecipeUsingPantry(plan, startDay, type, recipe, new ShoppingList());
    }

    public Optional<Recipe> findFirstCookableRecipe() {
        return recipeRepository.findAll()
                .stream()
                .filter(pantry::canMakeRecipe)
                .findFirst();
    }

    public boolean autoAssignFirstCookableRecipe(MealPlan plan,
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

        Optional<Recipe> maybeRecipe = recipeRepository.findAll()
                .stream()
                .filter(pantry::canMakeRecipe)
                .findFirst();

        if (maybeRecipe.isEmpty()) {
            System.out.println("autoAssignFirstCookableRecipe: no cookable recipe for "
                    + type + " on " + startDay);
            return false;
        }

        Recipe recipe = maybeRecipe.get();
        System.out.println("autoAssignFirstCookableRecipe: assigning " + recipe.getName()
                + " to " + startDay + " " + type);

        ShoppingList tmpList = new ShoppingList();
        boolean assigned = assignRecipeUsingPantry(plan, startDay, type, recipe, tmpList);
        System.out.println("  -> assigned? " + assigned);
        return assigned;
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

        var allRecipes = recipeRepository.findAll();
        if (allRecipes.isEmpty()) {
            System.out.println("autoAssignAnyRecipe: nessuna ricetta nel repository");
            return false;
        }

        var candidates = allRecipes.stream()
                .filter(r -> !wouldExceedDailyCalories(plan, startDay, r))
                .toList();

        if (candidates.isEmpty()) {
            System.out.println("autoAssignAnyRecipe: nessuna ricetta compatibile con il limite kcal per "
                    + startDay + " " + type);
            return false;
        }

        Recipe recipe = recipeSelectionStrategy.selectRecipe(
                candidates,
                plan,
                startDay,
                type,
                pantry
        );

        if (recipe == null) {
            return false;
        }

        return assignToPlan(plan, startDay, type, recipe);
    }


    public ShoppingList buildShoppingListForPlan(MealPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }

        Map<Ingredient, Double> totalRequired = new HashMap<>();

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

    private boolean assignToPlan(MealPlan plan,
                                 DayOfWeek day,
                                 MealType type,
                                 Recipe recipe) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        DayPlan dayPlan = plan.getDayPlan(day);
        if (dayPlan == null) {
            return false;
        }

        return dayPlan.tryAssignRecipe(type, recipe);
    }

    private double calculateDailyCalories(MealPlan plan, DayOfWeek day) {
        DayPlan dayPlan = plan.getDayPlan(day);
        if (dayPlan == null) {
            return 0.0;
        }

        double total = 0.0;
        for (MealSlot slot : dayPlan.getMeals()) {
            Recipe r = slot.getRecipe();
            if (r == null) {
                continue;
            }
            NutritionFacts nf = r.computeNutritionFacts();
            if (nf != null) {
                total += nf.getCalories();
            }
        }
        return total;
    }

    private boolean wouldExceedDailyCalories(MealPlan plan, DayOfWeek day, Recipe candidate) {
        if (!plan.hasCalorieLimit()) {
            return false; // nessun limite, tutto ok
        }

        NutritionFacts nf = candidate.computeNutritionFacts();
        if (nf == null) {
            // scelta: se c'è un limite, consideriamo 0 kcal se non abbiamo dati
            // (se vuoi essere più rigida, puoi restituire true qui)
            return false;
        }

        double current = calculateDailyCalories(plan, day);
        double candidateCalories = nf.getCalories();
        Double limit = plan.getMaxDailyCalories();

        return (limit != null) && (current + candidateCalories > limit);
    }

}
