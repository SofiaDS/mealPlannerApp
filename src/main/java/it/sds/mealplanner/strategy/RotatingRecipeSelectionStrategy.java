package it.sds.mealplanner.strategy;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;

/**
 * This class implements a strategy for selecting recipes for a meal plan.
 * It tries to choose recipes that have not been used on the same day and meal
 * type.
 * If there are no such recipes, it will choose any recipe that is compatible
 * with the meal type.
 * The selection is done in a rotating order, so that the same recipe is not
 * chosen twice in a row.
 */
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

    /**
     * Computes an index into the list of recipes given the day and the meal type.
     * The index is computed as (day.ordinal() * mealTypesCount + type.ordinal()) %
     * size,
     * where mealTypesCount is the number of meal types.
     * This ensures that the same recipe is not chosen twice in a row for the same
     * meal type and day.
     * 
     * @param day  the day of the week
     * @param type the meal type
     * @param size the size of the list of recipes
     * @return the computed index
     */
    private int computeIndex(DayOfWeek day, MealType type, int size) {
        int mealTypesCount = MealType.values().length;
        int slotIndex = day.ordinal() * mealTypesCount + type.ordinal();
        return Math.floorMod(slotIndex, size);
    }
}
