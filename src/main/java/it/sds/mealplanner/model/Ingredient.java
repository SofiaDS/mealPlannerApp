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
        this.name = name.trim(); //input sanitization
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", unit=" + unit +
                '}';
    }
    /*
    * TBD what if in different moments two ingredients with same name and unit are instantiated?
    * I want them to be treated as same ingredient, not different.
    * because i dont want to have the pantry with 10 ingredients called pasta
    */

}
