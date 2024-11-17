package recipes.linacy.recipesapp.model;

import lombok.Data;

@Data
public class IngredientForSteps {
    private int id;
    private String name;
    private String localizedName;
    private String image;
}
