package recipes.linacy.recipesapp.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;

import java.util.List;

import lombok.val;
import recipes.linacy.recipesapp.R;
import recipes.linacy.recipesapp.adapter.ApiClient;
import recipes.linacy.recipesapp.api.RecipeApi;
import recipes.linacy.recipesapp.model.Equipment;
import recipes.linacy.recipesapp.model.Ingredient;
import recipes.linacy.recipesapp.model.IngredientForSteps;
import recipes.linacy.recipesapp.model.RecipeDetails;
import recipes.linacy.recipesapp.model.RecipeInstruction;
import recipes.linacy.recipesapp.model.RecipeNutrition;
import recipes.linacy.recipesapp.model.Step;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    private TextView recipeTitle, recipeDescription, carbValue, fatValue, proteinValue, caloriesValue;
    private ImageView recipeImage, closeButton;
    private List<Ingredient> ingredients;

    private LinearLayout contentContainer;
    private Button buttonOption1, buttonOption2;
    private ViewStructure additionalInfo;

    private  List<Step> steps;
    TextView viewMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        initializeViews();

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        } else {
            showToast("Invalid recipe ID");
        }

        buttonOption1.setOnClickListener(v -> {
            showOption1Content();
            toggleButtonSelection(buttonOption1, buttonOption2);
        });

        buttonOption2.setOnClickListener(v -> {
            showOption2Content();
            toggleButtonSelection(buttonOption2, buttonOption1);
        });

        setRecipeDescriptionToggle();
        closeButton.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeDescription = findViewById(R.id.recipeDescription);
        val htmlContent = "<h2>Recipe Description</h2><p>This is a <b>bold</b> and <i>italic</i> example of a recipe description. Visit <a href='https://example.com'>this link</a> for more info.</p>";
        recipeDescription.setText(HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY));
        recipeDescription.setMovementMethod(LinkMovementMethod.getInstance());

        carbValue = findViewById(R.id.carbValue);
        fatValue = findViewById(R.id.fatValue);
        proteinValue = findViewById(R.id.proteinValue);
        caloriesValue = findViewById(R.id.caloriesValue);
        recipeImage = findViewById(R.id.recipeImage);
        closeButton = findViewById(R.id.closeButton);
        contentContainer = findViewById(R.id.contentContainer);
        buttonOption1 = findViewById(R.id.buttonOption1);
        buttonOption2 = findViewById(R.id.buttonOption2);
        viewMoreButton = findViewById(R.id.viewMoreButton);
    }

    private void setDefaultContent() {
        showOption1Content();
        recipeDescription.setMaxLines(2);
        recipeDescription.setEllipsize(TextUtils.TruncateAt.END);
    }
    private void showOption1Content() {
        contentContainer.removeAllViews();
        addIngredientsContent();
        addAdditionalInfoContent();
    }

    private void showOption2Content() {
        contentContainer.removeAllViews();
        addInstructionsContent();
    }
    private void setRecipeDescriptionToggle() {
        TextView viewMoreButton = findViewById(R.id.viewMoreButton);
        viewMoreButton.setOnClickListener(v -> {
            toggleDescription();
        });

        recipeDescription.setOnClickListener(v -> {
            if (recipeDescription.getMaxLines() == Integer.MAX_VALUE) {
                toggleDescription();
            }
        });
    }

    private void toggleDescription() {
        boolean isExpanded = recipeDescription.getMaxLines() == Integer.MAX_VALUE;
        if (isExpanded) {
            recipeDescription.setMaxLines(2);
            recipeDescription.setEllipsize(TextUtils.TruncateAt.END);
            viewMoreButton.setText("View More");
        } else {
            recipeDescription.setMaxLines(Integer.MAX_VALUE);
            recipeDescription.setEllipsize(null);
            viewMoreButton.setText("View Less");
        }
    }

    @Override
    public void onBackPressed() {
        if (recipeDescription.getMaxLines() == Integer.MAX_VALUE) {
            recipeDescription.setMaxLines(2);
            recipeDescription.setEllipsize(TextUtils.TruncateAt.END);
            viewMoreButton.setText("View More");
        } else {
            super.onBackPressed();
        }
    }

    private void toggleButtonSelection(Button selectedButton, Button unselectedButton) {

        selectedButton.setBackgroundColor(Color.parseColor("#042628"));
        unselectedButton.setBackgroundColor(Color.parseColor("#E6EBF2"));

        selectedButton.setTextColor(Color.parseColor("#FFFFFF"));
        unselectedButton.setTextColor(Color.parseColor("#042628"));
    }



    private void addIngredientsContent() {
        TextView ingredientsTitleView = new TextView(this);
        ingredientsTitleView.setText("Ingredients");
        ingredientsTitleView.setTextColor(Color.parseColor("#333333"));
        ingredientsTitleView.setTextSize(18);
        ingredientsTitleView.setTypeface(null, Typeface.BOLD);
        contentContainer.addView(ingredientsTitleView);

        TextView ingredientsListView = new TextView(this);


        StringBuilder ingredientsText = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            String ingredientText = ingredient.getName();
            if (ingredient.getAmount() != null) ingredientText += " - " + ingredient.getAmount();
            if (ingredient.getUnit() != null && !ingredient.getUnit().isEmpty())
                ingredientText += " " + ingredient.getUnit();
            ingredientsText.append(ingredientText).append("\n");
        }
        ingredientsListView.setText(ingredientsText.toString());

        ingredientsListView.setTextColor(Color.parseColor("#666666"));
        ingredientsListView.setTextSize(16);
        contentContainer.addView(ingredientsListView);
    }
    private void addInstructionsContent() {
        TextView instructionsTitleView = new TextView(this);
        instructionsTitleView.setText("Instructions");
        instructionsTitleView.setTextColor(Color.parseColor("#333333"));
        instructionsTitleView.setTextSize(20);
        instructionsTitleView.setTypeface(null, Typeface.BOLD);
        instructionsTitleView.setPadding(0, 16, 0, 16);
        contentContainer.addView(instructionsTitleView);

        for (Step step : steps) {
            CardView stepCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(16, 16, 16, 16);  // Margin around each card
            stepCard.setLayoutParams(cardParams);
            stepCard.setCardElevation(4);
            stepCard.setRadius(12);
            stepCard.setContentPadding(24, 24, 24, 24);

            LinearLayout stepContentLayout = new LinearLayout(this);
            stepContentLayout.setOrientation(LinearLayout.VERTICAL);

            TextView stepNumberView = new TextView(this);
            stepNumberView.setText("Step " + step.getNumber());
            stepNumberView.setTextColor(Color.parseColor("#333333"));
            stepNumberView.setTextSize(18);
            stepNumberView.setTypeface(null, Typeface.BOLD);
            stepNumberView.setPadding(0, 0, 0, 8);
            stepContentLayout.addView(stepNumberView);

            TextView stepInstructionView = new TextView(this);
            stepInstructionView.setText(step.getStep());
            stepInstructionView.setTextColor(Color.parseColor("#666666"));
            stepInstructionView.setTextSize(16);
            stepInstructionView.setPadding(0, 0, 0, 12);
            stepContentLayout.addView(stepInstructionView);

            if (!step.getIngredients().isEmpty()) {
                TextView ingredientsTitleView = new TextView(this);
                ingredientsTitleView.setText("Ingredients:");
                ingredientsTitleView.setTextColor(Color.parseColor("#333333"));
                ingredientsTitleView.setTextSize(16);
                ingredientsTitleView.setTypeface(null, Typeface.BOLD);
                ingredientsTitleView.setPadding(0, 8, 0, 4);
                stepContentLayout.addView(ingredientsTitleView);

                StringBuilder ingredientsText = new StringBuilder();
                for (IngredientForSteps ingredient : step.getIngredients()) {
                    ingredientsText.append("- ").append(ingredient.getName()).append("\n");
                }

                TextView ingredientsListView = new TextView(this);
                ingredientsListView.setText(ingredientsText.toString().trim());
                ingredientsListView.setTextColor(Color.parseColor("#666666"));
                ingredientsListView.setTextSize(16);
                ingredientsListView.setPadding(8, 0, 0, 12);
                stepContentLayout.addView(ingredientsListView);
            }

            if (!step.getEquipment().isEmpty()) {
                TextView equipmentTitleView = new TextView(this);
                equipmentTitleView.setText("Equipment:");
                equipmentTitleView.setTextColor(Color.parseColor("#333333"));
                equipmentTitleView.setTextSize(16);
                equipmentTitleView.setTypeface(null, Typeface.BOLD);
                equipmentTitleView.setPadding(0, 8, 0, 4);
                stepContentLayout.addView(equipmentTitleView);

                StringBuilder equipmentText = new StringBuilder();
                for (Equipment equipment : step.getEquipment()) {
                    equipmentText.append("- ").append(equipment.getName()).append("\n");
                }

                TextView equipmentListView = new TextView(this);
                equipmentListView.setText(equipmentText.toString().trim());
                equipmentListView.setTextColor(Color.parseColor("#666666"));
                equipmentListView.setTextSize(16);
                equipmentListView.setPadding(8, 0, 0, 12);
                stepContentLayout.addView(equipmentListView);
            }

            stepCard.addView(stepContentLayout);
            contentContainer.addView(stepCard);
        }

    }

    private void addAdditionalInfoContent() {
        TextView additionalInfoView = new TextView(this);
        additionalInfoView.setText("Additional Info");
        additionalInfoView.setTextColor(Color.parseColor("#666666"));
        additionalInfoView.setTextSize(16);
        contentContainer.addView(additionalInfoView);
    }


    private void loadRecipeDetails(int recipeId) {
        RecipeApi recipeApi = ApiClient.createRecipeApi();
        Call<RecipeDetails> detailsCall = recipeApi.getRecipeDetails(recipeId, true);
        Call<RecipeNutrition> nutritionCall = recipeApi.getNutrition(recipeId);

        detailsCall.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateRecipeDetails(response.body());
                    setDefaultContent();
                } else {
                    showToast("Failed to load recipe details");
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                showToast("Failed to load recipe details");
            }
        });

        nutritionCall.enqueue(new Callback<RecipeNutrition>() {
            @Override
            public void onResponse(Call<RecipeNutrition> call, Response<RecipeNutrition> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateNutritionDetails(response.body());
                } else {
                    showToast("Failed to load nutrition details");
                }
            }

            @Override
            public void onFailure(Call<RecipeNutrition> call, Throwable t) {
                showToast("Failed to load nutrition details");
            }
        });

        Call<List<RecipeInstruction>> stepsCall = recipeApi.getSteps(recipeId, true);

        stepsCall.enqueue(new Callback<List<RecipeInstruction>>() {
            @Override
            public void onResponse(Call<List<RecipeInstruction>> call, Response<List<RecipeInstruction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecipeInstruction> instructions = response.body();

                    if (!instructions.isEmpty()) {
                        steps = instructions.get(0).getSteps();
                    } else {
                        showToast("No steps available for this recipe.");
                    }
                } else {
                    showToast("Failed to load recipe details");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeInstruction>> call, Throwable t) {
                showToast("Failed to load recipe details");
            }
        });
    }

    private void populateRecipeDetails(RecipeDetails recipeDetails) {
        recipeTitle.setText(recipeDetails.getTitle());
        var summary = recipeDetails.getSummary();
        recipeDescription.setText(HtmlCompat.fromHtml(summary, HtmlCompat.FROM_HTML_MODE_LEGACY));
        recipeDescription.setMovementMethod(LinkMovementMethod.getInstance());
        Glide.with(this).load(recipeDetails.getImage()).into(recipeImage);
        ingredients = recipeDetails.getExtendedIngredients();
    }

    private void populateNutritionDetails(RecipeNutrition nutrition) {
        carbValue.setText("Carbs: " + nutrition.getCarbs());
        fatValue.setText("Fats: " + nutrition.getFat());
        proteinValue.setText("Proteins: " + nutrition.getProtein());
        caloriesValue.setText("Calories: " + nutrition.getCalories() + " kcal");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
