package recipes.linacy.recipesapp.model;

import java.util.List;

import lombok.Data;

@Data
public class RecipeInstruction {
    private String name;
    private List<Step> steps;

}
