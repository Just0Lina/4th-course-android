package recipes.linacy.recipesapp.model;

import java.util.List;

import lombok.Data;

@Data
public class RecipeResponse {
    private List<Recipe> results;
}
