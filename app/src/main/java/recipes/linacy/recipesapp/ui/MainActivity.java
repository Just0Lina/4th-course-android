package recipes.linacy.recipesapp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import recipes.linacy.recipesapp.HomeFragment;
import recipes.linacy.recipesapp.R;
import recipes.linacy.recipesapp.adapter.ApiClient;
import recipes.linacy.recipesapp.adapter.CardAdapter;
import recipes.linacy.recipesapp.adapter.RecipeAdapter;
import recipes.linacy.recipesapp.api.RecipeApi;
import recipes.linacy.recipesapp.databinding.ActivityMainBinding;
import recipes.linacy.recipesapp.model.CardItem;
import recipes.linacy.recipesapp.model.Recipe;
import recipes.linacy.recipesapp.model.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private RecyclerView cardRecyclerView, recipeRecyclerView;
    private CardAdapter cardAdapter;
    private RecipeAdapter recipeAdapter;

    private List<CardItem> cardItemList = new ArrayList<>();
    private List<Recipe> recipeList = new ArrayList<>();

    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigation();
        setupCardRecyclerView();
        setupRecipeRecyclerView();

        loadRecipes(currentPage);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                case R.id.discover:
                case R.id.notifications:
                case R.id.profile:
                    selectedFragment = new HomeFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            replaceFragment(selectedFragment);
            return true;
        });
    }

    private void setupCardRecyclerView() {
        cardRecyclerView = findViewById(R.id.recyclerViewCard);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cardItemList.addAll(createCardItems());
        cardAdapter = new CardAdapter(this, cardItemList);
        cardRecyclerView.setAdapter(cardAdapter);
        cardRecyclerView.addItemDecoration(createCardItemDecoration());
    }

    private void setupRecipeRecyclerView() {
        recipeRecyclerView = findViewById(R.id.recyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.addOnScrollListener(createOnScrollListener());
    }

    private List<CardItem> createCardItems() {

        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("Random food fact", R.drawable.lines, R.drawable.sample_image));
        cardItems.add(new CardItem("Random food Joke", R.drawable.lines, R.drawable.sample_image));
        return cardItems;
    }

    private RecyclerView.ItemDecoration createCardItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 16;
                outRect.left = 16;
            }
        };
    }


    private RecyclerView.OnScrollListener createOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    loadRecipes(currentPage * pageSize);
                }
            }
        };
    }

    private void loadRecipes(int page) {
        isLoading = true;
        Log.d(TAG, "Loading recipes for page " + page);

        RecipeApi recipeApi = ApiClient.createRecipeApi();
        Call<RecipeResponse> call = recipeApi.getRecipes(true, pageSize, page);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();
                    Log.d(TAG, "Number of recipes received: " + recipes.size());
                    recipeList.addAll(recipes);
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error loading recipes: " + response.code());
                    showToast("Error loading recipes");
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                isLoading = false;
                Log.e(TAG, "Failed to load recipes", t);
                showToast("Failed to load recipes");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout, fragment);
        fragmentTransaction.commit();
    }
}
