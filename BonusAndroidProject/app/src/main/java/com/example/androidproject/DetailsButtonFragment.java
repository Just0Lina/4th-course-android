package com.example.androidproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
public class DetailsButtonFragment extends Fragment {

    private static final String ARG_BUTTON_TEXT = "arg_button_text";

    public static DetailsButtonFragment newInstance(String buttonText) {
        DetailsButtonFragment fragment = new DetailsButtonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BUTTON_TEXT, buttonText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_button, container, false);
        Button button = view.findViewById(R.id.buttonDetails);
        String buttonText = getArguments() != null ? getArguments().getString(ARG_BUTTON_TEXT) : "No text";
        button.setText(buttonText);

        button.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentContainer, DetailsTextFragment.newInstance(buttonText));
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setMenuVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setMenuVisible(true);
        }
    }


}
