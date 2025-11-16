package it.sds.mealplanner.repository;

import it.sds.mealplanner.model.Recipe;

import java.util.List;

public interface RecipeRepository extends Repository<Recipe, String> {
    List<Recipe> findByNameContaining(String text);

}