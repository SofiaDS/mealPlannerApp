package it.sds.mealplanner.service;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.NutritionFacts;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.RecipeIngredient;
import it.sds.mealplanner.model.ShoppingList;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.strategy.RecipeSelectionStrategy;
import it.sds.mealplanner.strategy.RotatingRecipeSelectionStrategy;

/**
 * This class is responsible for planning meals based on a user's preferences.
 * It uses a {@link RecipeRepository} to retrieve recipes and a
 * {@link RecipeSelectionStrategy}
 * to select the best recipe for each meal.
 *
 * @author Sofia Della Spora
 * @since 1.0
 */
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

    /**
     * Assigns a recipe to a meal slot in the given meal plan, provided that the
     * recipe does not exceed the daily calorie limit.
     * The assignment is done starting from the given day and meal type.
     * If no recipe is found that respects the calorie limit, the method returns
     * false.
     * If no recipe is found in the repository, the method prints a message and
     * returns false.
     * If no recipe is found that is compatible with the given meal type, the method
     * prints a message and returns false.
     * 
     * @param plan     the meal plan to assign the recipe to
     * @param startDay the day to start assigning the recipe
     * @param type     the meal type to assign the recipe to
     * @return true if a recipe was assigned, false otherwise
     */
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
                pantry);

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

    public void autoAssignDayWithTwoSnacks(MealPlan plan, DayOfWeek day) {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }

        autoAssignAnyRecipe(plan, day, MealType.BREAKFAST);
        autoAssignAnyRecipe(plan, day, MealType.SNACK);
        autoAssignAnyRecipe(plan, day, MealType.LUNCH);
        autoAssignAnyRecipe(plan, day, MealType.SNACK);
        autoAssignAnyRecipe(plan, day, MealType.DINNER);
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
                total += nf.calories();
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
        double candidateCalories = nf.calories();
        Double limit = plan.getMaxDailyCalories();

        return (limit != null) && (current + candidateCalories > limit);
    }

}
