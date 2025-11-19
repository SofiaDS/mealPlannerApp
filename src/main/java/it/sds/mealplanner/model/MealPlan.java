package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class MealPlan {

    private final LocalDate startDate;
    private final Map<DayOfWeek, DayPlan> days = new EnumMap<>(DayOfWeek.class);

    public MealPlan(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void addDayPlan(DayPlan dayPlan) {
        if (dayPlan == null) {
            throw new IllegalArgumentException("DayPlan cannot be null");
        }
        days.put(dayPlan.getDay(), dayPlan);
    }

    public DayPlan getDayPlan(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("DayOfWeek cannot be null");
        }
        return days.get(day);
    }

    public Collection<DayPlan> getAllDayPlans() {
        return Collections.unmodifiableCollection(days.values());
    }

    public boolean assignRecipeAuto(DayOfWeek startDay, MealType type, Recipe recipe) {
        if (startDay == null) {
            throw new IllegalArgumentException("Start day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        DayOfWeek current = startDay;

        for (int i = 0; i < 7; i++) { // giro al massimo 1 settimana
            DayPlan dp = days.get(current);
            if (dp != null && dp.tryAssignRecipe(type, recipe)) {
                return true;
            }
            current = current.plus(1);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WEEK PLAN\n");
        for (DayOfWeek day : DayOfWeek.values()) {
            DayPlan dp = days.get(day);
            if (dp != null) {
                sb.append(dp.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
