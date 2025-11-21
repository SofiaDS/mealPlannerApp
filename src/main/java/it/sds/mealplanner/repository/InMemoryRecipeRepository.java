package it.sds.mealplanner.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import it.sds.mealplanner.model.Recipe;

public class InMemoryRecipeRepository implements RecipeRepository {

    private final Map<String, Recipe> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Recipe> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Recipe> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Recipe entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        storage.put(entity.getId(), entity);
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public List<Recipe> findByNameContaining(String text) {
        if (text == null || text.isBlank()) {
            return findAll();
        }
        String lower = text.toLowerCase();
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : storage.values()) {
            if (r.getName().toLowerCase().contains(lower)) {
                result.add(r);
            }
        }
        return result;
    }
}
