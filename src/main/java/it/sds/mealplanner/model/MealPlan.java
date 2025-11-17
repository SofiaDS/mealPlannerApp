package it.sds.mealplanner.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public void addDayPlan(DayPlan dayPlan) {
        days.put(dayPlan.getDay(), dayPlan);
    }

    public DayPlan getDayPlan(DayOfWeek day) {
        return days.get(day);
    }

    public boolean assignRecipeAuto(DayOfWeek startDay, MealType type, Recipe recipe) {
        DayOfWeek current = startDay;

        for (int i = 0; i < 7; i++) {
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
