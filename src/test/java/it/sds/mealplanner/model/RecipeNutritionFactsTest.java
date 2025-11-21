package it.sds.mealplanner.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeNutritionFactsTest {

    @Test
    void computeNutritionFacts_forGramBasedIngredient_shouldScaleFrom100g() {
        // Ingredient: 350 kcal per 100g
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));

        // Recipe: 80g di pasta
        Recipe pastaOnly = Recipe.create("Pasta test", MealType.LUNCH);
        pastaOnly.addIngredient(pasta, 80);

        NutritionFacts nf = pastaOnly.computeNutritionFacts();

        // 350 * (80/100) = 280 kcal
        assertEquals(280.0, nf.calories(), 0.01);
        // 12 * 0.8 = 9.6 g proteine
        assertEquals(9.6, nf.protein(), 0.01);
        // 70 * 0.8 = 56 g carbs
        assertEquals(56.0, nf.carbs(), 0.01);
        // 2 * 0.8 = 1.6 g fat
        assertEquals(1.6, nf.fat(), 0.01);
    }

    @Test
    void computeNutritionFacts_forPieceBasedIngredient_shouldScaleByPieces() {
        // Ingredient: 110 kcal per banana (per 1 PIECE)
        Ingredient banana = new Ingredient("Banana", Unit.PIECE);
        banana.setNutritionPerUnit(new NutritionFacts(110, 1, 28, 0.9));

        Recipe bananaSnack = Recipe.create("Banana snack test", MealType.SNACK);
        bananaSnack.addIngredient(banana, 2); // 2 banane

        NutritionFacts nf = bananaSnack.computeNutritionFacts();

        // 110 * 2 = 220 kcal
        assertEquals(220.0, nf.calories(), 0.01);
        // 1 * 2 = 2 g proteine
        assertEquals(2.0, nf.protein(), 0.01);
        // 28 * 2 = 56 g carbs
        assertEquals(56.0, nf.carbs(), 0.01);
        // 0.9 * 2 = 1.8 g fat
        assertEquals(1.8, nf.fat(), 0.01);
    }

    @Test
    void computeNutritionFacts_withMultipleIngredients_shouldSumContributions() {
        // Pasta: 350 kcal per 100g
        Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
        pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));

        // Ceci: 364 kcal per 100g
        Ingredient chickPeas = new Ingredient("Ceci", Unit.GRAM);
        chickPeas.setNutritionPerUnit(new NutritionFacts(364, 21.9, 63, 6));

        Recipe pastaCeci = Recipe.create("Pasta e ceci test", MealType.LUNCH);
        pastaCeci.addIngredient(pasta, 80);      // 80g pasta
        pastaCeci.addIngredient(chickPeas, 100); // 100g ceci

        NutritionFacts nf = pastaCeci.computeNutritionFacts();

        // Pasta: 350 * 0.8 = 280 kcal
        // Ceci : 364 * 1.0 = 364 kcal
        // Totale = 644 kcal
        assertEquals(644.0, nf.calories(), 0.1);

        // Proteine:
        // Pasta: 12 * 0.8 = 9.6
        // Ceci : 21.9 * 1.0 = 21.9
        // Totale = 31.5
        assertEquals(31.5, nf.protein(), 0.1);
    }
}
