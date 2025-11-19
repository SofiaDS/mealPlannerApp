package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.List;

public class RotatingRecipeSelectionStrategy implements RecipeSelectionStrategy {

    private int nextIndex = 0;

    @Override
    public Recipe selectRecipe(List<Recipe> recipes,
                               MealPlan plan,
                               DayOfWeek day,
                               MealType type,
                               Pantry pantry) {
        if (recipes == null || recipes.isEmpty()) {
            return null;
        }

        Recipe recipe = recipes.get(nextIndex % recipes.size());
        nextIndex++;
        return recipe;
    }
}
