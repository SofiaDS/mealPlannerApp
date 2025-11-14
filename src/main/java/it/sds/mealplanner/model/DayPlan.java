package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayPlan {

    private final DayOfWeek day;
    private final List<MealSlot> meals;

    public DayPlan(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        this.day = day;
        this.meals = new ArrayList<>();

        /*
        always create a fixed structure of mealSlot in a day
         */
        meals.add(new MealSlot(day, MealType.BREAKFAST, null));
        meals.add(new MealSlot(day, MealType.SNACK, null));
        meals.add(new MealSlot(day, MealType.LUNCH,null));
        meals.add(new MealSlot(day, MealType.SNACK,null));
        meals.add(new MealSlot(day, MealType.DINNER,null));
    }

    public DayOfWeek getDay() {
        return day;
    }
    /*
     * TBD when returning the day plan I want a fixed order breakfast - snack - lunch - snack - dinner
     * */
    public List<MealSlot> getMeals() {
        return Collections.unmodifiableList(meals);
    }
    public void assignRecipe(MealType type, Recipe recipe){
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null!");
        }

        if (type != MealType.SNACK){
            //case where a MealType can only be present one
            for (MealSlot slot : meals) {
                if (slot.getType() == type) {
                    slot.setRecipe(recipe);
                    return;
                }
            }
            throw new IllegalStateException("No slot of type " + type + " in this DayPlan");
        } else {
            //in case of snack need to choose the slot with recipe = null
            for (MealSlot slot : meals) {
                if (slot.getType() == MealType.SNACK && slot.getRecipe() == null) {
                    slot.setRecipe(recipe);
                    return;
                }
            }
            throw new IllegalStateException("All snack slots already used for day "+day);
        }
    }

    @Override
    public String toString() {
        return "DayPlan " + day +
                " meals=" + meals;
    }
}
