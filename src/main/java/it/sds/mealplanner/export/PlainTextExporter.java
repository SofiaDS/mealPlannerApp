package it.sds.mealplanner.export;

import it.sds.mealplanner.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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

            writer.write("Start date: " + plan.getStartDate().format(DATE_FMT));
            writer.newLine();
            writer.newLine();

            for (DayOfWeek day : DayOfWeek.values()) {
                DayPlan dayPlan = plan.getDayPlan(day);
                if (dayPlan == null) {
                    continue;
                }

                writer.write(day.name());
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
}
