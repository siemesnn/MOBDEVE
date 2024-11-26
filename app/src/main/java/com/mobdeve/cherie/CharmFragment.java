package com.mobdeve.cherie;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CharmFragment extends Fragment {

    private EditText funFactEditText;
    private EditText revealInfoEditText;
    private EditText favoriteThingsEditText;
    private RegisterInfoViewModel registerInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charm, container, false);

        funFactEditText = view.findViewById(R.id.funFactInput);
        revealInfoEditText = view.findViewById(R.id.revealInfoInput);
        favoriteThingsEditText = view.findViewById(R.id.favoriteThingsInput);

        registerInfoViewModel = new ViewModelProvider(requireActivity()).get(RegisterInfoViewModel.class);

        // Set up listeners to save data in real-time
        funFactEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setFunFact(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        revealInfoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setRevealInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        favoriteThingsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setFavoriteThings(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
}