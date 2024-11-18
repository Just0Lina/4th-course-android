package com.example.androidproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    public interface OnMenuItemClickListener {
        void onMenuItemClick(String text);
    }


//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnMenuItemClickListener) {
//            listener = (OnMenuItemClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnMenuItemClickListener");
//        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button buttonA = view.findViewById(R.id.buttonA);
        Button buttonB = view.findViewById(R.id.buttonB);
        Button buttonC = view.findViewById(R.id.buttonC);

        buttonA.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).onMenuItemClick("aaa");
            }
        });
        buttonB.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).onMenuItemClick("bbb");
            }
        });
        buttonC.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).onMenuItemClick("ccc");
            }
        });

        return view;
    }
}

