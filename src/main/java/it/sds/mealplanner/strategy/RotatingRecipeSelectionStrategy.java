package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<Recipe> usedRecipes = new HashSet<>();

        for (DayOfWeek d : DayOfWeek.values()) {
            DayPlan dayPlan = plan.getDayPlan(d);
            if (dayPlan == null) {
                continue;
            }
            for (MealSlot slot : dayPlan.getMeals()) {
                Recipe r = slot.getRecipe();
                if (r != null) {
                    usedRecipes.add(r);
                }
            }
        }

        for (Recipe candidate : allRecipes) {
            if (!usedRecipes.contains(candidate)) {
                return candidate;
            }
        }

        int mealTypesCount = MealType.values().length;
        int slotIndex = day.ordinal() * mealTypesCount + type.ordinal();

        int index = Math.floorMod(slotIndex, allRecipes.size());
        return allRecipes.get(index);
    }
}
