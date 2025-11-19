package it.sds.mealplanner.strategy;

import it.sds.mealplanner.model.*;

import java.time.DayOfWeek;
import java.util.List;

public interface RecipeSelectionStrategy {
    Recipe selectRecipe(List<Recipe> recipes,
                        MealPlan plan,
                        DayOfWeek day,
                        MealType type,
                        Pantry pantry);
}
