package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class DetailsTextFragment extends Fragment {

    public static final String ARG_TEXT = "arg_text";

    public static DetailsTextFragment newInstance(String text) {
        DetailsTextFragment fragment = new DetailsTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_text, container, false);
        TextView textView = view.findViewById(R.id.textViewDetails);
        String text = getArguments() != null ? getArguments().getString(ARG_TEXT) : "No text";
        textView.setText(text);
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
