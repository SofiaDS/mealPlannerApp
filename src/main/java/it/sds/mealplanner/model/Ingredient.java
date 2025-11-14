package it.sds.mealplanner.model;
import java.util.Objects;

public class Ingredient {
    private String name;
    private Unit unit;

    public Ingredient(String name, Unit unit){
        /*
        * TBD case in which params are not accepted
        * name or unit null ?
        * throw new IllegalArgumentException("custom message")
        */
        this.name = name.trim();
        this.unit = unit;
    }
}
