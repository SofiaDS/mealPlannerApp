package it.sds.mealplanner.app;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import it.sds.mealplanner.export.PlainTextExporter;
import it.sds.mealplanner.model.DayPlan;
import it.sds.mealplanner.model.DietaryTag;
import it.sds.mealplanner.model.Ingredient;
import it.sds.mealplanner.model.MealPlan;
import it.sds.mealplanner.model.MealSlot;
import it.sds.mealplanner.model.MealType;
import it.sds.mealplanner.model.NutritionFacts;
import it.sds.mealplanner.model.Pantry;
import it.sds.mealplanner.model.Recipe;
import it.sds.mealplanner.model.ShoppingList;
import it.sds.mealplanner.model.Unit;
import it.sds.mealplanner.model.UserPreferences;
import it.sds.mealplanner.repository.InMemoryRecipeRepository;
import it.sds.mealplanner.repository.RecipeRepository;
import it.sds.mealplanner.service.MealPlannerService;
import it.sds.mealplanner.strategy.SmartRecipeSelectionStrategy;

public class MealPlannerApp {

  public static void main(String[] args) {

    // --- Cereals / Carbs ---
    Ingredient pasta = new Ingredient("Pasta", Unit.GRAM);
    pasta.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 2));

    Ingredient semolinaPasta = new Ingredient("Pasta di semola", Unit.GRAM);
    semolinaPasta.setNutritionPerUnit(new NutritionFacts(360, 12, 70, 2));

    Ingredient eggPasta = new Ingredient("Pasta all'uovo secca", Unit.GRAM);
    eggPasta.setNutritionPerUnit(new NutritionFacts(360, 13, 70, 2));

    Ingredient freshEggPasta = new Ingredient("Pasta all'uovo fresca", Unit.GRAM);
    freshEggPasta.setNutritionPerUnit(new NutritionFacts(350, 13, 70, 2));

    Ingredient rice = new Ingredient("Rice", Unit.GRAM);
    rice.setNutritionPerUnit(new NutritionFacts(130, 2.7, 28, 0.3));

    Ingredient basmatiRice = new Ingredient("Riso basmati", Unit.GRAM);
    basmatiRice.setNutritionPerUnit(new NutritionFacts(130, 3, 28, 0.3));

    Ingredient venereRice = new Ingredient("Riso venere", Unit.GRAM);
    venereRice.setNutritionPerUnit(new NutritionFacts(130, 3, 28, 0.3));

    Ingredient couscous = new Ingredient("CousCous", Unit.GRAM);
    couscous.setNutritionPerUnit(new NutritionFacts(361, 13.7, 75, 1.1));

    Ingredient barley = new Ingredient("Barley", Unit.GRAM);
    barley.setNutritionPerUnit(new NutritionFacts(354, 12.5, 73.5, 2.3));

    Ingredient farroCereal = new Ingredient("Farro", Unit.GRAM);
    farroCereal.setNutritionPerUnit(new NutritionFacts(354, 12.5, 73.5, 2.3));

    Ingredient quinoa = new Ingredient("Quinoa", Unit.GRAM);
    quinoa.setNutritionPerUnit(new NutritionFacts(368, 14, 64, 6));

    Ingredient bread = new Ingredient("Bread", Unit.GRAM);
    bread.setNutritionPerUnit(new NutritionFacts(265, 9, 49, 3.2));
    
    Ingredient cornCakes = new Ingredient("Gallette mais", Unit.GRAM);
    cornCakes.setNutritionPerUnit(new NutritionFacts(380, 8, 84, 1));
    
    Ingredient wasaBread = new Ingredient("Fette integrali - Wasa", Unit.GRAM);
    wasaBread.setNutritionPerUnit(new NutritionFacts(350, 12, 70, 5));

    Ingredient potato = new Ingredient("Potato", Unit.GRAM);
    potato.setNutritionPerUnit(new NutritionFacts(77, 2, 17, 0.1));

    Ingredient gnocchi = new Ingredient("Gnocchi", Unit.GRAM);
    gnocchi.setNutritionPerUnit(new NutritionFacts(150, 3, 30, 0.5));

    Ingredient freshTortellini = new Ingredient("Tortellini freschi", Unit.GRAM);
    freshTortellini.setNutritionPerUnit(new NutritionFacts(220, 8, 42, 4));

    Ingredient dryTortellini = new Ingredient("Tortellini secchi", Unit.GRAM);
    dryTortellini.setNutritionPerUnit(new NutritionFacts(360, 12, 70, 2));

    Ingredient oats = new Ingredient("Oats", Unit.GRAM);
    oats.setNutritionPerUnit(new NutritionFacts(389, 16.9, 66.3, 6.9));

    Ingredient cornmeal = new Ingredient("Farina di mais", Unit.GRAM);
    cornmeal.setNutritionPerUnit(new NutritionFacts(380, 8, 78, 4));

    Ingredient cannedSweetCorn = new Ingredient("Mais dolce in scatola - sgocciolato", Unit.GRAM);
    cannedSweetCorn.setNutritionPerUnit(new NutritionFacts(96, 3, 23, 1));

    // --- Legumes / Plant proteins ---
    Ingredient chickPeas = new Ingredient("Chickpeas", Unit.GRAM);
    chickPeas.setNutritionPerUnit(new NutritionFacts(364, 21.9, 63, 6));

    Ingredient redBeans = new Ingredient("Red beans", Unit.GRAM);
    redBeans.setNutritionPerUnit(new NutritionFacts(337, 24.4, 60, 0.8));

    Ingredient lentils = new Ingredient("Lentils", Unit.GRAM);
    lentils.setNutritionPerUnit(new NutritionFacts(353, 25.8, 60, 1.1));

    Ingredient tofu = new Ingredient("Tofu", Unit.GRAM);
    tofu.setNutritionPerUnit(new NutritionFacts(76, 8, 1.9, 4.8));

    Ingredient tempeh = new Ingredient("Tempeh", Unit.GRAM);
    tempeh.setNutritionPerUnit(new NutritionFacts(193, 20.3, 7.6, 10.8));

    Ingredient seitan = new Ingredient("Seitan", Unit.GRAM);
    seitan.setNutritionPerUnit(new NutritionFacts(121, 21, 4, 2));

    // --- Meat ---
    Ingredient chicken = new Ingredient("Chicken", Unit.GRAM);
    chicken.setNutritionPerUnit(new NutritionFacts(165, 30, 0, 3.5));

    Ingredient beef = new Ingredient("Beef", Unit.GRAM);
    beef.setNutritionPerUnit(new NutritionFacts(250, 26, 0, 15));

    Ingredient pork = new Ingredient("Pork", Unit.GRAM);
    pork.setNutritionPerUnit(new NutritionFacts(242, 27, 0, 14));

    Ingredient turkey = new Ingredient("Turkey", Unit.GRAM);
    turkey.setNutritionPerUnit(new NutritionFacts(135, 29, 0, 1.5));

    Ingredient rabbit = new Ingredient("Rabbit", Unit.GRAM);
    rabbit.setNutritionPerUnit(new NutritionFacts(173, 28, 0, 7.6));

    // --- Fish ---
    Ingredient salmon = new Ingredient("Salmon", Unit.GRAM);
    salmon.setNutritionPerUnit(new NutritionFacts(208, 20, 0, 13));

    Ingredient tuna = new Ingredient("Tuna", Unit.GRAM);
    tuna.setNutritionPerUnit(new NutritionFacts(132, 28, 0, 1.3));

    Ingredient cod = new Ingredient("Cod", Unit.GRAM);
    cod.setNutritionPerUnit(new NutritionFacts(105, 23, 0, 0.9));

    Ingredient hake = new Ingredient("Hake", Unit.GRAM);
    hake.setNutritionPerUnit(new NutritionFacts(112, 20, 0, 3.4));

    Ingredient seabass = new Ingredient("Seabass", Unit.GRAM);
    seabass.setNutritionPerUnit(new NutritionFacts(97, 20.2, 0, 2.7));

    Ingredient sardine = new Ingredient("Sardine", Unit.GRAM);
    sardine.setNutritionPerUnit(new NutritionFacts(208, 24, 0, 11));

    // --- Cheese / Dairy ---
    Ingredient ricotta = new Ingredient("Ricotta", Unit.GRAM);
    ricotta.setNutritionPerUnit(new NutritionFacts(174, 11.8, 6.5, 13.2));

    Ingredient mozzarella = new Ingredient("Mozzarella", Unit.GRAM);
    mozzarella.setNutritionPerUnit(new NutritionFacts(280, 28, 3, 17));

    Ingredient lightMozzarella = new Ingredient("Mozzarella Light", Unit.GRAM);
    lightMozzarella.setNutritionPerUnit(new NutritionFacts(280, 28, 3, 17));

    Ingredient mozzarellaBocconcini = new Ingredient("Bocconcini di mozzarella magra", Unit.GRAM);
    mozzarellaBocconcini.setNutritionPerUnit(new NutritionFacts(280, 28, 3, 17));

    Ingredient feta = new Ingredient("Feta", Unit.GRAM);
    feta.setNutritionPerUnit(new NutritionFacts(264, 14, 4, 21));

    Ingredient parmesan = new Ingredient("Parmesan", Unit.GRAM);
    parmesan.setNutritionPerUnit(new NutritionFacts(431, 38, 4.1, 29));

    Ingredient grana = new Ingredient("Grana", Unit.GRAM);
    grana.setNutritionPerUnit(new NutritionFacts(431, 37.7, 4.5, 28));

    Ingredient agedCheese = new Ingredient("Formaggi stagionati (Parmigiano, Grana, Pecorino, Scamorza)", Unit.GRAM);
    agedCheese.setNutritionPerUnit(new NutritionFacts(431, 38, 4.1, 29));

    Ingredient primosale = new Ingredient("Primosale", Unit.GRAM);
    primosale.setNutritionPerUnit(new NutritionFacts(300, 20, 0, 20));

    Ingredient lowFatCheese = new Ingredient("Fiocchi di formaggio magro", Unit.GRAM);
    lowFatCheese.setNutritionPerUnit(new NutritionFacts(120, 11, 1, 8));

    Ingredient philadelphiaLight = new Ingredient("Philadelphia Light", Unit.GRAM);
    philadelphiaLight.setNutritionPerUnit(new NutritionFacts(150, 9, 5, 8));

    Ingredient tominoCheese = new Ingredient("Tomino", Unit.GRAM);
    tominoCheese.setNutritionPerUnit(new NutritionFacts(210, 14, 1, 16));

    Ingredient crescenza = new Ingredient("Crescenza", Unit.GRAM);
    crescenza.setNutritionPerUnit(new NutritionFacts(250, 9, 3.5, 19));

    Ingredient stracchino = new Ingredient("Stracchino", Unit.GRAM);
    stracchino.setNutritionPerUnit(new NutritionFacts(275, 14, 3, 24));

    Ingredient fullFatMilk = new Ingredient("Full-fat milk", Unit.GRAM);
    fullFatMilk.setNutritionPerUnit(new NutritionFacts(61, 3.2, 5, 3.3));

    Ingredient greekYogurt = new Ingredient("Greek yogurt", Unit.GRAM);
    greekYogurt.setNutritionPerUnit(new NutritionFacts(63, 10, 4, 0.5));

    // --- Eggs ---
    Ingredient eggs = new Ingredient("Eggs", Unit.PIECE);
    eggs.setNutritionPerUnit(new NutritionFacts(65, 6.3, 0.34, 4.9));

    // --- Dairy alternatives ---
    Ingredient almondMilk = new Ingredient("Almond milk", Unit.GRAM);
    almondMilk.setNutritionPerUnit(new NutritionFacts(13, 0.5, 1.5, 1));

    Ingredient oatMilk = new Ingredient("Oat milk", Unit.GRAM);
    oatMilk.setNutritionPerUnit(new NutritionFacts(47, 1, 6, 1.5));

    // --- Yogurt variants ---
    Ingredient yogurtFage = new Ingredient("Yogurt 0% - Fage", Unit.GRAM);
    yogurtFage.setNutritionPerUnit(new NutritionFacts(59, 10, 3.6, 0.4));

    Ingredient yogurtGreekNoFat = new Ingredient("Yogurt Greco 0% Grassi", Unit.GRAM);
    yogurtGreekNoFat.setNutritionPerUnit(new NutritionFacts(63, 10, 4, 0.5));

    Ingredient yogurtZymilGreek = new Ingredient("Yogurt Zymil alla Greca", Unit.GRAM);
    yogurtZymilGreek.setNutritionPerUnit(new NutritionFacts(60, 8, 3, 0.5));

    Ingredient yogurtHiproDanone = new Ingredient("Yogurt Hipro Danone", Unit.GRAM);
    yogurtHiproDanone.setNutritionPerUnit(new NutritionFacts(60, 10, 3, 0.3));

    Ingredient yogurtFruitLowFat = new Ingredient("Yogurt alla Frutta Magro", Unit.GRAM);
    yogurtFruitLowFat.setNutritionPerUnit(new NutritionFacts(50, 5, 8, 0.5));

    Ingredient proteinYogurt = new Ingredient("Yogurt Proteico Magro", Unit.GRAM);
    proteinYogurt.setNutritionPerUnit(new NutritionFacts(55, 10, 6, 0.3));

    // --- Breakfast carbs / snacks ---
    Ingredient wholeWheatBiscuits = new Ingredient("Fette Biscottate Integrali", Unit.GRAM);
    wholeWheatBiscuits.setNutritionPerUnit(new NutritionFacts(370, 13, 70, 4));

    Ingredient wholeWheatBread = new Ingredient("Pane di Tipo Integrale", Unit.GRAM);
    wholeWheatBread.setNutritionPerUnit(new NutritionFacts(265, 9, 49, 3.2));

    Ingredient cornFlakes = new Ingredient("Corn flakes", Unit.GRAM);
    cornFlakes.setNutritionPerUnit(new NutritionFacts(357, 8, 84.5, 1));

    Ingredient oatsFlakes = new Ingredient("Fiocchi d'Avena", Unit.GRAM);
    oatsFlakes.setNutritionPerUnit(new NutritionFacts(389, 16.9, 66.3, 6.9));

    Ingredient riceFlakes = new Ingredient("Fiocchi di Riso", Unit.GRAM);
    riceFlakes.setNutritionPerUnit(new NutritionFacts(374, 7, 80, 1));

    Ingredient farroFlakes = new Ingredient("Fiocchi di Farro", Unit.GRAM);
    farroFlakes.setNutritionPerUnit(new NutritionFacts(365, 14, 70, 3));

    Ingredient muesli = new Ingredient("Muesli", Unit.GRAM);
    muesli.setNutritionPerUnit(new NutritionFacts(400, 15, 65, 5));

    // --- Cookies / sweets ---
    Ingredient enerzonaBiscuits = new Ingredient("Frollini Enerzona", Unit.GRAM);
    enerzonaBiscuits.setNutritionPerUnit(new NutritionFacts(450, 10, 60, 15));

    Ingredient biscuitsMarie = new Ingredient("Biscotti Secchi (Tipo Orosaiwa)", Unit.GRAM);
    biscuitsMarie.setNutritionPerUnit(new NutritionFacts(400, 8, 80, 10));

    Ingredient marmaladeLowSugar = new Ingredient("Marmellata a ridotto tenore di zucchero", Unit.GRAM);
    marmaladeLowSugar.setNutritionPerUnit(new NutritionFacts(190, 0.5, 50, 0));

    Ingredient honey = new Ingredient("Miele", Unit.GRAM);
    honey.setNutritionPerUnit(new NutritionFacts(304, 0.3, 82.4, 0));

    // --- Vegetables ---
    Ingredient spinach = new Ingredient("Spinach", Unit.GRAM);
    spinach.setNutritionPerUnit(new NutritionFacts(23, 2.8, 3.6, 0.4));

    Ingredient zucchini = new Ingredient("Zucchini", Unit.GRAM);
    zucchini.setNutritionPerUnit(new NutritionFacts(17, 1.2, 3.1, 0.3));

    Ingredient pumpkin = new Ingredient("Pumpkin", Unit.GRAM);
    pumpkin.setNutritionPerUnit(new NutritionFacts(26, 1, 3.5, 0.1));

    Ingredient cucumber = new Ingredient("Cucumber", Unit.GRAM);
    cucumber.setNutritionPerUnit(new NutritionFacts(16, 0.7, 3.6, 0.1));

    Ingredient tomato = new Ingredient("Tomato", Unit.GRAM);
    tomato.setNutritionPerUnit(new NutritionFacts(18, 0.9, 3.9, 0.2));

    Ingredient bellPepper = new Ingredient("Bell pepper", Unit.GRAM);
    bellPepper.setNutritionPerUnit(new NutritionFacts(31, 1, 6, 0.3));

    Ingredient onion = new Ingredient("Onion", Unit.GRAM);
    onion.setNutritionPerUnit(new NutritionFacts(40, 1.1, 9.3, 0.1));

    Ingredient carrot = new Ingredient("Carrot", Unit.GRAM);
    carrot.setNutritionPerUnit(new NutritionFacts(41, 0.9, 10, 0.2));

    Ingredient celery = new Ingredient("Celery", Unit.GRAM);
    celery.setNutritionPerUnit(new NutritionFacts(16, 0.7, 3, 0.2));

    Ingredient lettuce = new Ingredient("Lettuce", Unit.GRAM);
    lettuce.setNutritionPerUnit(new NutritionFacts(21, 1.8, 2.97, 0.4));

    // --- Fruits ---
    Ingredient apple = new Ingredient("Apple", Unit.PIECE);
    apple.setNutritionPerUnit(new NutritionFacts(64, 0.6, 17, 0.15));

    Ingredient banana = new Ingredient("Banana", Unit.PIECE);
    banana.setNutritionPerUnit(new NutritionFacts(110, 1, 28, 9));

    Ingredient kiwi = new Ingredient("Kiwi", Unit.PIECE);
    kiwi.setNutritionPerUnit(new NutritionFacts(42, 0.8, 10, 0.4));

    Ingredient orange = new Ingredient("Orange", Unit.PIECE);
    orange.setNutritionPerUnit(new NutritionFacts(70, 1.3, 16.5, 0.2));

    Ingredient strawberry = new Ingredient("Strawberry", Unit.GRAM);
    strawberry.setNutritionPerUnit(new NutritionFacts(32, 0.7, 7.7, 0.3));

    Ingredient blueberry = new Ingredient("Blueberry", Unit.GRAM);
    blueberry.setNutritionPerUnit(new NutritionFacts(57, 0.7, 14, 0.3));

    Ingredient pear = new Ingredient("Pear", Unit.PIECE);
    pear.setNutritionPerUnit(new NutritionFacts(96, 0.6, 26.7, 0.2));

    Ingredient pineapple = new Ingredient("Pineapple", Unit.GRAM);
    pineapple.setNutritionPerUnit(new NutritionFacts(50, 0.5, 13, 0.1));

    Ingredient grapes = new Ingredient("Grapes", Unit.GRAM);
    grapes.setNutritionPerUnit(new NutritionFacts(69, 0.7, 18, 0.2));

    Ingredient mango = new Ingredient("Mango", Unit.GRAM);
    mango.setNutritionPerUnit(new NutritionFacts(60, 0.8, 15, 0.4));
    // --- Recipes ---
    // --- SNACKS ---
    Recipe s1 = Recipe.create("Apple snack", MealType.SNACK);
    s1.addIngredient(apple, 1);
    s1.addTags(List.of(DietaryTag.VEGAN, DietaryTag.LOW_FAT));
    
    Recipe s2 = Recipe.create("Banana snack", MealType.SNACK);
    s2.addIngredient(banana, 1);
    s2.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s3 = Recipe.create("Pear snack", MealType.SNACK);
    s3.addIngredient(pear, 1);
    s3.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s4 = Recipe.create("Orange snack", MealType.SNACK);
    s4.addIngredient(orange, 1);
    s4.addTags(List.of(DietaryTag.VEGAN, DietaryTag.LOW_FAT));
    
    Recipe s5 = Recipe.create("Kiwi snack", MealType.SNACK);
    s5.addIngredient(kiwi, 1);
    s5.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s6 = Recipe.create("Grapes snack", MealType.SNACK);
    s6.addIngredient(grapes, 150);
    s6.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s7 = Recipe.create("Pineapple snack", MealType.SNACK);
    s7.addIngredient(pineapple, 200);
    s7.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s8 = Recipe.create("Mango snack", MealType.SNACK);
    s8.addIngredient(mango, 150);
    s8.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s9 = Recipe.create("Strawberry snack", MealType.SNACK);
    s9.addIngredient(strawberry, 150);
    s9.addTags(List.of(DietaryTag.VEGAN, DietaryTag.LOW_FAT));
    
    Recipe s10 = Recipe.create("Blueberry snack", MealType.SNACK);
    s10.addIngredient(blueberry, 150);
    s10.addTags(List.of(DietaryTag.VEGAN, DietaryTag.LOW_FAT));
    
    Recipe s11 = Recipe.create("Greek yogurt snack", MealType.SNACK);
    s11.addIngredient(greekYogurt, 150);
    s11.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe s12 = Recipe.create("Protein yogurt snack", MealType.SNACK);
    s12.addIngredient(proteinYogurt, 160);
    s12.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe s13 = Recipe.create("Corn cakes snack", MealType.SNACK);
    s13.addIngredient(cornCakes, 30);
    s13.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe s14 = Recipe.create("Biscuits snack", MealType.SNACK);
    s14.addIngredient(biscuitsMarie, 30);
    s14.addTags(List.of(DietaryTag.VEGETARIAN));


    // --- BREAKFAST ---
    Recipe b1 = Recipe.create("Greek yogurt + corn flakes + low sugar jam", MealType.BREAKFAST);
    b1.addIngredient(greekYogurt, 150);
    b1.addIngredient(cornFlakes, 30);
    b1.addIngredient(marmaladeLowSugar, 30);
    b1.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe b2 = Recipe.create("Yogurt Fage 0% + oats + honey", MealType.BREAKFAST);
    b2.addIngredient(yogurtFage, 170);
    b2.addIngredient(oatsFlakes, 30);
    b2.addIngredient(honey, 12);
    b2.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT));
    
    Recipe b3 = Recipe.create("Yogurt Hipro + muesli + honey", MealType.BREAKFAST);
    b3.addIngredient(yogurtHiproDanone, 160);
    b3.addIngredient(muesli, 30);
    b3.addIngredient(honey, 12);
    b3.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe b4 = Recipe.create("Yogurt Zymil Greek + rice flakes + low sugar jam", MealType.BREAKFAST);
    b4.addIngredient(yogurtZymilGreek, 150);
    b4.addIngredient(riceFlakes, 30);
    b4.addIngredient(marmaladeLowSugar, 30);
    b4.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe b5 = Recipe.create("Almond milk + corn flakes + honey", MealType.BREAKFAST);
    b5.addIngredient(almondMilk, 200);
    b5.addIngredient(cornFlakes, 30);
    b5.addIngredient(honey, 12);
    b5.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe b6 = Recipe.create("Oat milk + oats + low sugar jam", MealType.BREAKFAST);
    b6.addIngredient(oatMilk, 200);
    b6.addIngredient(oatsFlakes, 30);
    b6.addIngredient(marmaladeLowSugar, 30);
    b6.addTags(List.of(DietaryTag.VEGAN));
    
    Recipe b7 = Recipe.create("Whole wheat bread + low sugar jam", MealType.BREAKFAST);
    b7.addIngredient(wholeWheatBread, 50);
    b7.addIngredient(marmaladeLowSugar, 30);
    b7.addTags(List.of(DietaryTag.VEGAN));

    // --- MAIN MEALS ---
    Recipe m1 = Recipe.create("Pasta with chickpeas and tomato", MealType.LUNCH);
    m1.addIngredient(pasta, 80);
    m1.addIngredient(chickPeas, 100);
    m1.addIngredient(tomato, 150);
    m1.addTags(List.of(DietaryTag.VEGAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m2 = Recipe.create("Rice with chicken and zucchini", MealType.DINNER);
    m2.addIngredient(rice, 60);
    m2.addIngredient(chicken, 200);
    m2.addIngredient(zucchini, 150);
    m2.addTags(List.of(DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));
    
    Recipe m3 = Recipe.create("Quinoa with tofu and peppers", MealType.LUNCH);
    m3.addIngredient(quinoa, 60);
    m3.addIngredient(tofu, 150);
    m3.addIngredient(bellPepper, 150);
    m3.addTags(List.of(DietaryTag.VEGAN, DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));
    
    Recipe m4 = Recipe.create("Barley with tuna and tomato", MealType.DINNER);
    m4.addIngredient(barley, 60);
    m4.addIngredient(tuna, 150);
    m4.addIngredient(tomato, 150);
    m4.addTags(List.of(DietaryTag.PESCATARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m5 = Recipe.create("Couscous with lentils and spinach", MealType.LUNCH);
    m5.addIngredient(couscous, 60);
    m5.addIngredient(lentils, 100);
    m5.addIngredient(spinach, 150);
    m5.addTags(List.of(DietaryTag.VEGAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m6 = Recipe.create("Potatoes with beef and lettuce", MealType.DINNER);
    m6.addIngredient(potato, 250);
    m6.addIngredient(beef, 200);
    m6.addIngredient(lettuce, 150);
    m6.addTags(List.of(DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));
    
    Recipe m7 = Recipe.create("Farro with tempeh and zucchini", MealType.LUNCH);
    m7.addIngredient(farroCereal, 60);
    m7.addIngredient(tempeh, 150);
    m7.addIngredient(zucchini, 150);
    m7.addTags(List.of(DietaryTag.VEGAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m8 = Recipe.create("Rice basmati with salmon and spinach", MealType.DINNER);
    m8.addIngredient(basmatiRice, 60);
    m8.addIngredient(salmon, 200);
    m8.addIngredient(spinach, 150);
    m8.addTags(List.of(DietaryTag.PESCATARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m9 = Recipe.create("Gnocchi with ricotta and tomato", MealType.LUNCH);
    m9.addIngredient(gnocchi, 150);
    m9.addIngredient(ricotta, 100);
    m9.addIngredient(tomato, 150);
    m9.addTags(List.of(DietaryTag.VEGETARIAN));
    
    Recipe m10 = Recipe.create("Egg pasta with pork and peppers", MealType.DINNER);
    m10.addIngredient(eggPasta, 60);
    m10.addIngredient(pork, 200);
    m10.addIngredient(bellPepper, 150);
    m10.addTags(List.of(DietaryTag.HIGH_PROTEIN));
    
    Recipe m11 = Recipe.create("Bread with eggs and spinach", MealType.LUNCH);
    m11.addIngredient(bread, 70);
    m11.addIngredient(eggs, 2);
    m11.addIngredient(spinach, 150);
    m11.addTags(List.of(DietaryTag.VEGETARIAN, DietaryTag.HIGH_PROTEIN));
    
    Recipe m12 = Recipe.create("Quinoa with seitan and pumpkin", MealType.DINNER);
    m12.addIngredient(quinoa, 60);
    m12.addIngredient(seitan, 150);
    m12.addIngredient(pumpkin, 200);
    m12.addTags(List.of(DietaryTag.VEGAN, DietaryTag.HIGH_PROTEIN, DietaryTag.LOW_FAT));
    
    Recipe m13 = Recipe.create("Couscous with mozzarella and tomato", MealType.LUNCH);
    m13.addIngredient(couscous, 60);
    m13.addIngredient(mozzarella, 150);
    m13.addIngredient(tomato, 150);
    m13.addTags(List.of(DietaryTag.VEGETARIAN));
    
    Recipe m14 = Recipe.create("Potatoes with cod and zucchini", MealType.DINNER);
    m14.addIngredient(potato, 250);
    m14.addIngredient(cod, 200);
    m14.addIngredient(zucchini, 150);
    m14.addTags(List.of(DietaryTag.PESCATARIAN, DietaryTag.HIGH_PROTEIN, DietaryTag.GLUTEN_FREE));


    // --- Recipe Repository ---
    RecipeRepository recipeRepo = new InMemoryRecipeRepository();
    
    // Breakfast (7)
    recipeRepo.save(b1);
    recipeRepo.save(b2);
    recipeRepo.save(b3);
    recipeRepo.save(b4);
    recipeRepo.save(b5);
    recipeRepo.save(b6);
    recipeRepo.save(b7);
    
    // Snacks (14)
    recipeRepo.save(s1);
    recipeRepo.save(s2);
    recipeRepo.save(s3);
    recipeRepo.save(s4);
    recipeRepo.save(s5);
    recipeRepo.save(s6);
    recipeRepo.save(s7);
    recipeRepo.save(s8);
    recipeRepo.save(s9);
    recipeRepo.save(s10);
    recipeRepo.save(s11);
    recipeRepo.save(s12);
    recipeRepo.save(s13);
    recipeRepo.save(s14);
    
    // Main meals (14)
    recipeRepo.save(m1);
    recipeRepo.save(m2);
    recipeRepo.save(m3);
    recipeRepo.save(m4);
    recipeRepo.save(m5);
    recipeRepo.save(m6);
    recipeRepo.save(m7);
    recipeRepo.save(m8);
    recipeRepo.save(m9);
    recipeRepo.save(m10);
    recipeRepo.save(m11);
    recipeRepo.save(m12);
    recipeRepo.save(m13);
    recipeRepo.save(m14);

    // --- Pantry ---
    Pantry pantry = new Pantry();

    // --- Meal plan for the whole week ---
    MealPlan plan = new MealPlan(LocalDate.now(), 2000.0);
    for (DayOfWeek day: DayOfWeek.values()) {
      plan.addDayPlan(new DayPlan(day));
    }

    // --- User preferences ---
    UserPreferences prefs = new UserPreferences();

    prefs.addPreferredTag(DietaryTag.VEGETARIAN);
    prefs.addPreferredTag(DietaryTag.HIGH_PROTEIN);

    prefs.addExcludedIngredient(orata);
    prefs.setMaxSameRecipePerWeek(2);
    prefs.setAvoidSameRecipeOnConsecutiveDays(true);

    // --- Service ---
    SmartRecipeSelectionStrategy strategy = new SmartRecipeSelectionStrategy(prefs);
    MealPlannerService service = new MealPlannerService(pantry, recipeRepo, strategy);
    // MealPlannerService service = new MealPlannerService(pantry, recipeRepo);

    for (DayOfWeek day: DayOfWeek.values()) {
      for (MealType type: MealType.values()) {
        service.autoAssignDayWithTwoSnacks(plan, day);
      }
    }

    System.out.println("===== MEAL PLAN =====");
    for (DayOfWeek day: DayOfWeek.values()) {
      DayPlan dayPlan = plan.getDayPlan(day);
      System.out.println("\n" + day + ":");
      if (dayPlan == null) {
        System.out.println("  (nessun pasto)");
        continue;
      }

      for (MealSlot slot: dayPlan.getMeals()) {
        MealType type = slot.getType();
        Recipe recipe = slot.getRecipe();
        String recipeName = (recipe != null) ? recipe.getName() : "(vuoto)";
        System.out.println("  " + type + " -> " + recipeName);
      }
    }

    // Shopping list globale
    ShoppingList globalShoppingList = service.buildShoppingListForPlan(plan);

    System.out.println("\n===== SHOPPING LIST GLOBALE =====");
    Map < Ingredient, Double > items = globalShoppingList.getItems();

    if (items.isEmpty()) {
      System.out.println("Nessun ingrediente da comprare: la pantry copre tutto il piano!");
    } else {
      for (Map.Entry < Ingredient, Double > entry: items.entrySet()) {
        Ingredient ing = entry.getKey();
        double qty = entry.getValue();
        System.out.println("- " + ing.getName() + ": " + qty + " " + ing.getUnit());
      }
    }

    PlainTextExporter exporter = new PlainTextExporter();
    DateTimeFormatter fileDateFmt = DateTimeFormatter.ofPattern("yyyyMMdd");
    String fileName = "mealplan_" + plan.getStartDate().format(fileDateFmt) + ".txt";
    Path outputFile = Path.of(fileName);

    try {
      exporter.export(plan, globalShoppingList, outputFile);
      System.out.println("\nFile esportato: " + outputFile.toAbsolutePath());
    } catch (IOException e) {
      System.err.println("Errore durante l'esportazione del piano: " + e.getMessage());
    }

    // System.out.println("DEBUG NUTRITION:");
    // System.out.println(" " + pastaCeci.getName() + " -> " +
    // pastaCeci.computeNutritionFacts().getCalories() + " kcal");
    // System.out.println(" " + bananaY.getName() + " -> " +
    // bananaY.computeNutritionFacts().getCalories() + " kcal");
  }
}
