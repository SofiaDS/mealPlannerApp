package it.sds.mealplanner.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A recipe is a set of ingredients for preparing a meal.
 * Each recipe has a unique identifier, a name, and a list of ingredients.
 * The ingredients are stored in a list of RecipeIngredient objects, which
 * contain the amount of each ingredient needed for the recipe.
 * The recipe also has a preferred meal type, which is one of the values
 * in the MealType enum.
 * The recipe can also have a set of tags, which are values in the DietaryTag
 * enum. The tags are used to describe the recipe and can be used to filter
 * recipes based on dietary restrictions.
 */
public class Recipe {

    private static int NEXT_ID = 1; // i want to assign id automatically

    private final String id;
    private String name;
    private final List<RecipeIngredient> ingredients;
    private final MealType preferredMealType;

    private final Set<DietaryTag> tags;

    // private constructor - dont want to use it externally
    private Recipe(String id, String name, MealType preferredMealType) {
        // input check
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Recipe id cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Recipe name cannot be null or blank");
        }
        // input sanitization
        this.id = id.trim();
        this.name = name.trim();
        this.ingredients = new ArrayList<>();
        this.preferredMealType = Objects.requireNonNull(preferredMealType, "preferredMealType cannot be null");
        this.tags = new HashSet<>();
    }

    public static Recipe create(String name) {
        // default di backward-compatibility: consideriamo LUNCH se non specificato
        return create(name, MealType.LUNCH);
    }

    public static Recipe create(String name, MealType preferredMealType) {
        String id = "R" + NEXT_ID++; // es: R1, R2, R3...
        return new Recipe(id, name, preferredMealType);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MealType getPreferredMealType() {
        return preferredMealType;
    }

    public List<RecipeIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);

    }

    public void addIngredient(Ingredient ingredient, double quantityRequired) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (quantityRequired <= 0) {
            throw new IllegalArgumentException("Quantity required must be > 0");
        }
        RecipeIngredient r1 = new RecipeIngredient(ingredient, quantityRequired);
        ingredients.add(r1);

    }

    public Set<DietaryTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTag(DietaryTag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        tags.add(tag);
    }

    public void addTags(Collection<DietaryTag> tagsToAdd) {
        if (tagsToAdd == null) {
            throw new IllegalArgumentException("Tags collection cannot be null");
        }
        for (DietaryTag tag : tagsToAdd) {
            if (tag == null) {
                throw new IllegalArgumentException("Tag in collection cannot be null");
            }
        }
        tags.addAll(tagsToAdd);
    }

    public NutritionFacts computeNutritionFacts() {
        double totalKcal = 0.0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;

        for (RecipeIngredient ri : ingredients) {
            Ingredient ing = ri.getIngredient();
            double qty = ri.getQuantityRequired();
            NutritionFacts perUnit = ing.getNutritionPerUnit();

            if (perUnit == null) {
                continue;
            }

            double factor = computeFactor(ing.getUnit(), qty);

            totalKcal += perUnit.calories() * factor;
            totalProtein += perUnit.protein() * factor;
            totalCarbs += perUnit.carbs() * factor;
            totalFat += perUnit.fat() * factor;
        }

        return new NutritionFacts(totalKcal, totalProtein, totalCarbs, totalFat);
    }

    private double computeFactor(Unit unit, double qty) {
        return switch (unit) {
            case GRAM -> qty / 100.0;
            case PIECE -> qty;
            default -> qty;
        };
    }

    @Override
    public String toString() {
        return "Recipe " +
                "id='" + id + '\'' +
                ", name='" + name + '\'';
    }
}
