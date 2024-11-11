package recipes.linacy.recipesapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import recipes.linacy.recipesapp.HomeFragment;
import recipes.linacy.recipesapp.R;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import recipes.linacy.recipesapp.adapter.RecipeAdapter;
import recipes.linacy.recipesapp.api.RecipeApi;
import recipes.linacy.recipesapp.api.RetrofitClient;
import recipes.linacy.recipesapp.databinding.ActivityMainBinding;
import recipes.linacy.recipesapp.model.Recipe;
import recipes.linacy.recipesapp.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private static final String TAG = "MainActivity";
    private List<Recipe> recipeList;
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isLoading = false;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.discover:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.notifications:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new HomeFragment());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this,recipeList);
        recyclerView.setAdapter(recipeAdapter);
        loadRecipes(currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    loadRecipes(currentPage * pageSize);
                }

            }
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void loadRecipes(int page) {
        isLoading = true;
        Log.d(TAG, "loadRecipes: Loading recipes for page " + page);

        RecipeApi recipeApi = RetrofitClient.getInstance().create(RecipeApi.class);
        Call<RecipeResponse> call = recipeApi.getRecipes("", true, pageSize, page);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();
                    Log.d(TAG, "onResponse: Number of recipes received: " + recipes.size());

                    recipeList.addAll(recipes);
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onResponse: Error loading recipes - " + response.code());
                    Toast.makeText(MainActivity.this, "Error loading recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                isLoading = false;
                Log.e(TAG, "onFailure: Failed to load recipes" + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
