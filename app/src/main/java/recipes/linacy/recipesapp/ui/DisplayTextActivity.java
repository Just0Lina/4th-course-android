package recipes.linacy.recipesapp.ui;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import recipes.linacy.recipesapp.R;

public class DisplayTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        TextView textView = findViewById(R.id.textView);

        String textData = getIntent().getStringExtra("textData");

        textView.setText(textData);
    }
}

