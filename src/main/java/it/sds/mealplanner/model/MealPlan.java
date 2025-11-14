package it.sds.mealplanner.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
* weekly plan 7 days 5 recipes per day
* */

public class MealPlan {

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

    @Override
    public String toString() {
        return "MealPlan{" +
                "startDate=" + startDate +
                ", days=" + days +
                '}';
    }
}

