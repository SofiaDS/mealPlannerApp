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
    public static final String VERSION = "MealPlan v6";

    public MealPlan(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
    }

    public void addDayPlan(DayPlan dayPlan) {
        if (dayPlan == null) {
            throw new IllegalArgumentException("DayPlan cannot be null");
        }
        days.put(dayPlan.getDay(), dayPlan);
    }

    public DayPlan getDayPlan(DayOfWeek day) {
        return days.get(day);
    }

    public Collection<DayPlan> getAllDayPlans() {
        return Collections.unmodifiableCollection(days.values());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean assignRecipeAuto(DayOfWeek day, MealType type, Recipe recipe) {
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Meal type cannot be null");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        DayPlan dayPlan = days.get(day);
        if (dayPlan == null) {
            return false;
        }

        return dayPlan.tryAssignRecipe(type, recipe);
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
