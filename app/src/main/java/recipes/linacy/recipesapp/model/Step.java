package recipes.linacy.recipesapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Step {
    private int number;

    private String step;
    private List<Equipment> equipment;

    private List<IngredientForSteps> ingredients;
}