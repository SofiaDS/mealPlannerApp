package it.sds.mealplanner.export;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.NutritionFacts;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.ShoppingList;

/**
 * Exports a meal plan to a plain text file.
 *
 * @author Sofia Della Spora
 * @version 1.0
 */
public class PlainTextExporter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void export(MealPlan plan, ShoppingList shoppingList, Path outputFile) throws IOException {
        if (plan == null) {
            throw new IllegalArgumentException("MealPlan cannot be null");
        }
        if (shoppingList == null) {
            throw new IllegalArgumentException("ShoppingList cannot be null");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }

        try (var writer = Files.newBufferedWriter(outputFile)) {

            // date
            writer.write("Start date: " + plan.getStartDate().format(DATE_FMT));
            writer.newLine();
            writer.newLine();

            // MEAL PLAN
            for (DayOfWeek day : DayOfWeek.values()) {
                DayPlan dayPlan = plan.getDayPlan(day);
                if (dayPlan == null) {
                    continue;
                }

                double dailyKcal = calculateDailyCalories(plan, day);

                writer.write(day.name() + " (kcal: " + Math.round(dailyKcal) + ")");
                writer.newLine();

                for (MealSlot slot : dayPlan.getMeals()) {
                    MealType type = slot.getType();
                    Recipe recipe = slot.getRecipe();
                    String recipeName = (recipe != null) ? recipe.getName() : "(vuoto)";

                    writer.write("\t" + type + " -> " + recipeName);
                    writer.newLine();
                }

                writer.newLine();
            }

            // SHOPPING LIST
            writer.write("Shopping list:");
            writer.newLine();

            Map<Ingredient, Double> items = shoppingList.getItems();
            if (items.isEmpty()) {
                writer.write("- (empty: pantry covers the whole plan)");
                writer.newLine();
            } else {
                for (Map.Entry<Ingredient, Double> entry : items.entrySet()) {
                    Ingredient ing = entry.getKey();
                    double qty = entry.getValue();
                    writer.write("- " + ing.getName() + ": " + qty + " " + ing.getUnit());
                    writer.newLine();
                }
            }
        }
    }

    private double calculateDailyCalories(MealPlan plan, DayOfWeek day) {
        DayPlan dayPlan = plan.getDayPlan(day);
        if (dayPlan == null) {
            return 0.0;
        }

        double total = 0.0;
        for (MealSlot slot : dayPlan.getMeals()) {
            Recipe r = slot.getRecipe();
            if (r == null) {
                continue;
            }
            NutritionFacts nf = r.computeNutritionFacts();
            if (nf != null) {
                total += nf.calories();
            }
        }
        return total;
    }
}
