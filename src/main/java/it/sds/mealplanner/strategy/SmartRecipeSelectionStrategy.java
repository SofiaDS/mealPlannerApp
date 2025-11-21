package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.List;

public class SmartRecipeSelectionStrategy implements RecipeSelectionStrategy {

    private final UserPreferences preferences;

    public SmartRecipeSelectionStrategy(UserPreferences preferences) {
        if (preferences == null) {
            throw new IllegalArgumentException("UserPreferences cannot be null");
        }
        this.preferences = preferences;
    }

    @Override
    public Recipe selectRecipe(List<Recipe> candidates,
                               MealPlan plan,
                               DayOfWeek day,
                               MealType type,
                               Pantry pantry) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        Recipe best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Recipe r : candidates) {
            double score = scoreRecipe(r, plan, day, type);
            if (score > bestScore) {
                bestScore = score;
                best = r;
            }
        }

        return best;
    }

    private double scoreRecipe(Recipe recipe,
                               MealPlan plan,
                               DayOfWeek day,
                               MealType type) {

        if (violatesForbiddenTags(recipe) ||
                containsExcludedIngredient(recipe) ||
                exceedsMaxRepetitions(plan, recipe)) {
            return Double.NEGATIVE_INFINITY;
        }

        double score = 0.0;

        if (!preferences.getPreferredTags().isEmpty()) {
            long matches = recipe.getTags().stream()
                    .filter(preferences.getPreferredTags()::contains)
                    .count();

            if (matches > 0) {
                score += 50 + 10 * matches;
            } else {
                score -= 30;
            }
        }

        if (preferences.isAvoidSameRecipeOnConsecutiveDays()
                && isUsedPreviousDaySameType(plan, recipe, day, type)) {
            score -= 40;
        }

        int timesUsed = countUsageInWeek(plan, recipe);
        score -= timesUsed * 10;

        if (recipe.getPreferredMealType() == type) {
            score += 20;
        }

        return score;
    }

    private boolean violatesForbiddenTags(Recipe recipe) {
        if (preferences.getForbiddenTags().isEmpty()) {
            return false;
        }
        for (DietaryTag tag : recipe.getTags()) {
            if (preferences.getForbiddenTags().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsExcludedIngredient(Recipe recipe) {
        if (preferences.getExcludedIngredients().isEmpty()) {
            return false;
        }
        for (RecipeIngredient ri : recipe.getIngredients()) {
            if (preferences.getExcludedIngredients().contains(ri.getIngredient())) {
                return true;
            }
        }
        return false;
    }

    private boolean exceedsMaxRepetitions(MealPlan plan, Recipe recipe) {
        int used = countUsageInWeek(plan, recipe);
        return used >= preferences.getMaxSameRecipePerWeek();
    }

    private int countUsageInWeek(MealPlan plan, Recipe recipe) {
        int count = 0;
        for (DayOfWeek d : DayOfWeek.values()) {
            DayPlan dp = plan.getDayPlan(d);
            if (dp == null) {
                continue;
            }
            for (MealSlot slot : dp.getMeals()) {
                if (recipe.equals(slot.getRecipe())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isUsedPreviousDaySameType(MealPlan plan,
                                              Recipe recipe,
                                              DayOfWeek day,
                                              MealType type) {
        DayOfWeek previous = day.minus(1);
        if (previous == day) {
            return false;
        }
        DayPlan prevDayPlan = plan.getDayPlan(previous);
        if (prevDayPlan == null) {
            return false;
        }
        for (MealSlot slot : prevDayPlan.getMeals()) {
            if (slot.getType() == type && recipe.equals(slot.getRecipe())) {
                return true;
            }
        }
        return false;
    }
}
