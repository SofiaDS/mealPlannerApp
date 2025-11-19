package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class RotatingRecipeSelectionStrategy implements RecipeSelectionStrategy {

    @Override
    public Recipe selectRecipe(List<Recipe> allRecipes,
                               MealPlan plan,
                               DayOfWeek day,
                               MealType type,
                               Pantry pantry) {
        if (allRecipes == null || allRecipes.isEmpty()) {
            return null;
        }

        List<Recipe> compatible = allRecipes.stream()
                .filter(r -> isSuitableForMealType(r, type))
                .collect(Collectors.toList());

        if (compatible.isEmpty()) {
            compatible = new ArrayList<>(allRecipes);
        }

        Set<Recipe> usedToday = getRecipesUsedForDay(plan, day);

        List<Recipe> notUsedToday = compatible.stream()
                .filter(r -> !usedToday.contains(r))
                .collect(Collectors.toList());

        if (!notUsedToday.isEmpty()) {
            int index = computeIndex(day, type, notUsedToday.size());
            return notUsedToday.get(index);
        }

        int index = computeIndex(day, type, compatible.size());
        return compatible.get(index);
    }

    private boolean isSuitableForMealType(Recipe recipe, MealType slotType) {
        MealType pref = recipe.getPreferredMealType();

        return switch (slotType) {
            case BREAKFAST -> pref == MealType.BREAKFAST;
            case SNACK -> (pref == MealType.SNACK || pref == MealType.BREAKFAST);
            case LUNCH, DINNER -> (pref == MealType.LUNCH || pref == MealType.DINNER);
        };
    }

    private Set<Recipe> getRecipesUsedForDay(MealPlan plan, DayOfWeek day) {
        Set<Recipe> used = new HashSet<>();

        DayPlan dayPlan = plan.getDayPlan(day);
        if (dayPlan == null) {
            return used;
        }

        for (MealSlot slot : dayPlan.getMeals()) {
            Recipe r = slot.getRecipe();
            if (r != null) {
                used.add(r);
            }
        }
        return used;
    }

    private int computeIndex(DayOfWeek day, MealType type, int size) {
        int mealTypesCount = MealType.values().length;
        int slotIndex = day.ordinal() * mealTypesCount + type.ordinal();
        return Math.floorMod(slotIndex, size);
    }
}
