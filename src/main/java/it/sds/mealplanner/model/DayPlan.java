package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
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
    }

    public DayOfWeek getDay() {
        return day;
    }

    public List<MealSlot> getMeals() {
        return meals;
    }

    public void addMeal(MealSlot mealSlot) {
        if (mealSlot == null) {
            throw new IllegalArgumentException("MealSlot cannot be null");
        }
        // TBD check that in a day there is no double lunch/dinner/breakfast
        meals.add(mealSlot);
    }

    @Override
    public String toString() {
        return "DayPlan{" +
                "day=" + day +
                ", meals=" + meals +
                '}';
    }
}
