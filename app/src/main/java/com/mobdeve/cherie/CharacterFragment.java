package com.mobdeve.cherie;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CharacterFragment extends Fragment {

    private Spinner locationSpinner;
    private EditText bioEditText;
    private EditText hobbyEditText;
    private EditText heightEditText;
    private RadioGroup genderGroup;

    private RegisterInfoViewModel registerInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.character, container, false);

        locationSpinner = view.findViewById(R.id.locationSpinner);
        bioEditText = view.findViewById(R.id.bioInput);
        hobbyEditText = view.findViewById(R.id.hobbyInput);
        heightEditText = view.findViewById(R.id.heightInput);
        genderGroup = view.findViewById(R.id.genderGroup);

        // List of cities in Manila
        String[] citiesInManila = {"Manila", "Quezon City", "Caloocan", "Makati", "Pasig", "Taguig", "Mandaluyong", "Marikina", "Parañaque", "Las Piñas"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, citiesInManila);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        registerInfoViewModel = new ViewModelProvider(requireActivity()).get(RegisterInfoViewModel.class);

        // Set up listeners to save data in real-time
        bioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setBio(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        hobbyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setHobby(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerInfoViewModel.getUserData().setHeight(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedGenderButton = view.findViewById(checkedId);
            registerInfoViewModel.getUserData().setGender(selectedGenderButton.getText().toString());
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                registerInfoViewModel.getUserData().setLocation(citiesInManila[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}