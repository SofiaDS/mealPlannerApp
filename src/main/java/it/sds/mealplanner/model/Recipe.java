package it.sds.mealplanner.model;

public class Recipe {

    private static int NEXT_ID = 1; //i want to assign id automatically

    private final String id;
    private String name;

    // private constructor - dont want to use it externally
    private Recipe(String id, String name) {
        //input check
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Recipe id cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Recipe name cannot be null or blank");
        }
        //input sanitization
        this.id = id.trim();
        this.name = name.trim();
    }

    /*
    *  obj creation delegated to method to automatically generate ids - factory method
    * usage:
    * Recipe r1 = Recipe.create("Pasta e ceci");
    * */
    public static Recipe create(String name) {
        String id = "R" + NEXT_ID++;  // es: R1, R2, R3...
        return new Recipe(id, name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Recipe name cannot be null or blank");
        }
        this.name = name.trim();
    }

    @Override
    public String toString() {
        return "Recipe " +
                "id='" + id + '\'' +
                ", name='" + name + '\'' ;
    }
}
