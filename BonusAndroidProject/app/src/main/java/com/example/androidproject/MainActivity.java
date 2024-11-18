package com.example.androidproject;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    public static final String FIRST_ELEMENT = "FIRST_ELEMENT";

    private boolean isInLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new MenuFragment())
                    .commit();

            if (isInLandscape) {
                onMenuItemClick("aaa");
            }
        }

    }

    public void setMenuVisible(boolean isVisible) {
        if (!isInLandscape) {
            View viewById = findViewById(R.id.fragmentContainer);
            viewById.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
    public void onMenuItemClick(String text) {
        getSupportFragmentManager().popBackStack(FIRST_ELEMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer, DetailsButtonFragment.newInstance(text))
                .addToBackStack(FIRST_ELEMENT)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (isInLandscape) {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                finish();
                return;
            }
        }
        super.onBackPressed();
    }
}
