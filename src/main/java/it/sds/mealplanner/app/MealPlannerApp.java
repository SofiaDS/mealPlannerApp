package it.sds.mealplanner.app;

import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;

public class MealPlannerApp {

    public static void main(String[] args) {
        RecipeRepository recipeRepo = new InMemoryRecipeRepository();

        Recipe pastaCeci = Recipe.create("Pasta e ceci");
        Recipe oatmeal = Recipe.create("Oatmeal proteico");
        recipeRepo.save(pastaCeci);
        recipeRepo.save(oatmeal);

        System.out.println("All recipes in repo:");
        for (Recipe r : recipeRepo.findAll()) {
            System.out.println("- " + r.getId() + " :: " + r.getName());
        }

        System.out.println("\nRecipes containing 'pasta':");
        for (Recipe r : recipeRepo.findByNameContaining("pasta")) {
            System.out.println("- " + r.getName());
        }

    }
}
