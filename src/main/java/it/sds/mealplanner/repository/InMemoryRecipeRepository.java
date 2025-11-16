package it.sds.mealplanner.repository;

import it.sd.mealplanner.model.Recipe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class inMemoryRecipeRepository implements RecipeRepository {

    private final MapsString, Recipe> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Recipe> findById(String id) {
        return Optional.ofNullable(storage.get(id));

    }

    @Override
    public List<Recipe> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Recipe entity){
        if(entity == null){
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        storage.put(entity.getId(), entity);
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
    
    @Override
    public List<Recipe> findByNameContaining(String text){
        if (text == null || text.isEmpty()){
            return findAll();
        }
        String lower = text.toLowerCase();
        List<Recipe> result = new ArrayList<>();

        for (Recipe r : storage.values()) {
            if (r.getName().toLowerCase.contains(lower)) {
                result.add(r);
            }

        }
        return result
    }
}