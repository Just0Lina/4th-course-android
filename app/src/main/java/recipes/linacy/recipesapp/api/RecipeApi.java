package recipes.linacy.recipesapp.api;

import java.util.List;

import recipes.linacy.recipesapp.model.ApiResponse;
import recipes.linacy.recipesapp.model.RecipeDetails;
import recipes.linacy.recipesapp.model.RecipeInstruction;
import recipes.linacy.recipesapp.model.RecipeNutrition;
import recipes.linacy.recipesapp.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface RecipeApi {

    @GET("recipes/complexSearch")
    Call<RecipeResponse> getRecipes(
            @Query("limitLicense") boolean limitLicense,
            @Query("number") int number,
            @Query("offset") int offset
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeDetails(
            @Path("id") int recipeId,
            @Query("includeNutrition") boolean includeNutrition
    );

    @GET("recipes/{id}/nutritionWidget.json")
    Call<RecipeNutrition> getNutrition(
            @Path("id") int recipeId
    );

    @GET("recipes/{id}/analyzedInstructions")
    Call<List<RecipeInstruction>> getSteps(
            @Path("id") int recipeId,
            @Query("stepBreakdown") boolean stepBreakdown
    );

    @GET("food/jokes/random")
    Call<ApiResponse> getRandomJoke(
    );

    @GET("food/trivia/random")
    Call<ApiResponse> getRandomFact(
    );



}
