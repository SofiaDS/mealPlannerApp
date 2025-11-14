package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MealPlan implements Iterable<MealSlot> {

    private final LocalDate startDate;
    private final List<DayPlan> days;

    public MealPlan(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
        this.days = new ArrayList<>();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<DayPlan> getDays() {
        return days;
    }

    public void addDayPlan(DayPlan dayPlan) {
        if (dayPlan == null) {
            throw new IllegalArgumentException("DayPlan cannot be null");
        }
        days.add(dayPlan);
    }

    public List<MealSlot> getAllMeals() {
        List<MealSlot> result = new ArrayList<>();
        for (DayPlan dayPlan : days) {
            result.addAll(dayPlan.getMeals());
        }
        return result;
    }

    @Override
    public Iterator<MealSlot> iterator() {
        return getAllMeals().iterator();
    }

    private DayPlan findDayPlan(DayOfWeek day) {
        for (DayPlan dp : days) {
            if (dp.getDay() == day) {
                return dp;
            }
        }
        return null;
    }

    /**
     * Try to assign a recipe starting from startDay:
     * - if that day has a free slot, use it
     * - otherwise move to the next day, up to 7 attempts
     */
    public void assignRecipeAuto(DayOfWeek startDay, MealType type, Recipe recipe) {
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }

        DayOfWeek current = startDay;
        int attempts = 0;

        while (attempts < 7) { // 7 days / attempts
            DayPlan dayPlan = findDayPlan(current);
            if (dayPlan != null) {
                boolean assigned = dayPlan.tryAssignRecipe(type, recipe);
                if (assigned) {
                    return;
            }
            }

            current = current.plus(1); // LUN->MAR->...->DOM->LUN
            attempts++;
        }

        throw new IllegalStateException("No free slot for " + type + " in the whole week");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meal plan starting ").append(startDate).append("\n\n");
        for (DayPlan dayPlan : days) {
            sb.append(dayPlan).append("\n");
        }
        return sb.toString();
    }
}
