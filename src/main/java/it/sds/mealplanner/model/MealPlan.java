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
    private final Double maxDailyCalories;

    public MealPlan(LocalDate startDate) {
        this(startDate, null);
    }

    public MealPlan(LocalDate startDate, Double maxDailyCalories) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
        this.maxDailyCalories = maxDailyCalories;
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
        return days.get(day);
    }

    public Collection<DayPlan> getAllDayPlans() {
        return Collections.unmodifiableCollection(days.values());
    }

    public Double getMaxDailyCalories() {
        return maxDailyCalories;
    }

    public boolean hasCalorieLimit() {
        return maxDailyCalories != null;
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
