package recipes.linacy.recipesapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import recipes.linacy.recipesapp.R;
import recipes.linacy.recipesapp.api.RecipeApi;
import recipes.linacy.recipesapp.api.RetrofitClient;
import recipes.linacy.recipesapp.model.Recipe;
import recipes.linacy.recipesapp.model.RecipeDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";
    private TextView recipeTitle, recipeDescription, healthScore, dietInfo, preparationTime, servings, sourceUrl;
    private ImageView recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeDescription = findViewById(R.id.recipeDescription);
        healthScore = findViewById(R.id.healthScore);
        dietInfo = findViewById(R.id.dietInfo);
        preparationTime = findViewById(R.id.preparationTime);
        servings = findViewById(R.id.servings);
        sourceUrl = findViewById(R.id.sourceUrl);
        recipeImage = findViewById(R.id.recipeImage);
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);  // Get the recipeId from the Intent
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecipeDetails(int recipeId) {
        RecipeApi recipeApi = RetrofitClient.getInstance().create(RecipeApi.class);
        Call<RecipeDetails> call = recipeApi.getRecipeDetails( recipeId, "663aa51f0aa54f61ab6726779af1db6f",true);

        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetails recipeDetails = response.body();

                    recipeTitle.setText(recipeDetails.getTitle());
                    recipeDescription.setText(recipeDetails.getSummary());
                    healthScore.setText("Health Score: " + recipeDetails.getHealthScore());
                    dietInfo.setText("Dietary Info: " + recipeDetails.getDiets());
//                    preparationTime.setText("Preparation Time: " + recipeDetails.getPreparationMinutes() + " minutes");
                    servings.setText("Servings: " + recipeDetails.getServings());
//                    sourceUrl.setText("Source: " + recipeDetails.getSourceName());

                    // Set the image using Glide
                    Glide.with(RecipeDetailActivity.this)
                            .load(recipeDetails.getImage())
                            .into(recipeImage);

                    // Make the source URL clickable
                    sourceUrl.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeDetails.getSourceUrl()));
                        startActivity(intent);
                    });
                } else {
                    Log.e(TAG, "Error fetching recipe details");
                    Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                Log.e(TAG, "Failed to load recipe details", t);
                Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
