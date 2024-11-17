package recipes.linacy.recipesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import recipes.linacy.recipesapp.R;
import recipes.linacy.recipesapp.api.RecipeApi;
import recipes.linacy.recipesapp.model.ApiResponse;
import recipes.linacy.recipesapp.model.CardItem;
import recipes.linacy.recipesapp.model.Recipe;
import recipes.linacy.recipesapp.ui.DisplayTextActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardItem> cardItems;

    private Context context;

    public CardAdapter(Context context, List<CardItem> cardItems) {
        this.context = context;
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);

        holder.text.setText(cardItem.getText());
        holder.image1.setImageResource(cardItem.getImage1());
        holder.image2.setImageResource(cardItem.getImage2());

        holder.itemView.setOnClickListener(v -> {
            if (cardItems.stream().anyMatch(c->c.getText().equals(cardItem.getText()))) {
                onCardItemClicked(cardItem);
            }
        });
    }


    public void onCardItemClicked(CardItem cardItem) {

        RecipeApi recipeApi = ApiClient.createRecipeApi();
        Call<ApiResponse> call;
        if (cardItem.getText().equals("Random food fact")) {
            call = recipeApi.getRandomFact();
        } else {
            call = recipeApi.getRandomJoke();
        }
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String text = response.body().getText();

                    Intent intent = new Intent(context, DisplayTextActivity.class);
                    intent.putExtra("textData", text);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public interface OnCardItemClickListener {
        void onCardItemClicked(CardItem cardItem);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView image1;
        ImageView image2;
        TextView text;

        public CardViewHolder(View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            text = itemView.findViewById(R.id.text);
        }
    }
}
