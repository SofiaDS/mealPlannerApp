package it.sds.mealplanner.export;

import it.sds.mealplanner.model.MealPlan;

import java.io.IOException;
import java.nio.file.Path;

public interface MealPlanExporter {

    void export(MealPlan plan, Path outputFile) throws IOException;
}
