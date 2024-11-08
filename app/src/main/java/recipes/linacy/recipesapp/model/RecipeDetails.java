package recipes.linacy.recipesapp.model;

import java.util.List;

import lombok.Data;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDetails {
    private String title;
    private String image;
    private String imageType;
    private String summary;
    private int servings;
    private int readyInMinutes;
    private String sourceUrl;
    private String healthScore;
    private List<String> diets;
    private List<String> dishTypes;
    private List<Ingredient> extendedIngredients;
    private String instructions;
    private List<Step> analyzedInstructions;
    }
