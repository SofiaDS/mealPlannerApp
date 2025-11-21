package it.sds.mealplanner.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserPreferences {

    private final Set<DietaryTag> preferredTags = new HashSet<>();
    private final Set<DietaryTag> forbiddenTags = new HashSet<>();
    private final Set<Ingredient> excludedIngredients = new HashSet<>();
    private int maxSameRecipePerWeek = Integer.MAX_VALUE;
    private boolean avoidSameRecipeOnConsecutiveDays = true;

    public Set<DietaryTag> getPreferredTags() {
        return Collections.unmodifiableSet(preferredTags);
    }

    public Set<DietaryTag> getForbiddenTags() {
        return Collections.unmodifiableSet(forbiddenTags);
    }

    public Set<Ingredient> getExcludedIngredients() {
        return Collections.unmodifiableSet(excludedIngredients);
    }

    public int getMaxSameRecipePerWeek() {
        return maxSameRecipePerWeek;
    }

    public void setMaxSameRecipePerWeek(int maxSameRecipePerWeek) {
        if (maxSameRecipePerWeek <= 0) {
            throw new IllegalArgumentException("maxSameRecipePerWeek must be > 0");
        }
        this.maxSameRecipePerWeek = maxSameRecipePerWeek;
    }

    public boolean isAvoidSameRecipeOnConsecutiveDays() {
        return avoidSameRecipeOnConsecutiveDays;
    }

    public void setAvoidSameRecipeOnConsecutiveDays(boolean avoidSameRecipeOnConsecutiveDays) {
        this.avoidSameRecipeOnConsecutiveDays = avoidSameRecipeOnConsecutiveDays;
    }

    public void addPreferredTag(DietaryTag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        preferredTags.add(tag);
    }

    public void addForbiddenTag(DietaryTag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        forbiddenTags.add(tag);
    }

    public void addExcludedIngredient(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        excludedIngredients.add(ingredient);
    }
}
