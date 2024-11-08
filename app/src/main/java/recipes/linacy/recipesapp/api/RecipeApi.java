package recipes.linacy.recipesapp.api;

import recipes.linacy.recipesapp.model.RecipeDetails;
import recipes.linacy.recipesapp.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> getRecipes(
            @Query("apiKey") String apiKey,
            @Query("limitLicense") boolean limitLicense,
            @Query("number") int number,
            @Query("offset") int offset

    );

    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeDetails(
            @Path("id") int recipeId,
            @Query("apiKey") String apiKey,
            @Query("includeNutrition") boolean includeNutrition
    );
}