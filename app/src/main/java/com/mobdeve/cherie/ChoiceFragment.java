package com.mobdeve.cherie;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ChoiceFragment extends Fragment {

    private RadioGroup genderPreferenceGroup;
    private CheckBox casualDatingCheckBox;
    private CheckBox longTermCheckBox;
    private CheckBox marriageCheckBox;

    private RegisterInfoViewModel registerInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choice, container, false);

        genderPreferenceGroup = view.findViewById(R.id.genderPreferenceGroup);
        casualDatingCheckBox = view.findViewById(R.id.intentionCasualDating);
        longTermCheckBox = view.findViewById(R.id.intentionLongTerm);
        marriageCheckBox = view.findViewById(R.id.intentionMarriage);

        registerInfoViewModel = new ViewModelProvider(requireActivity()).get(RegisterInfoViewModel.class);

        // Set up listeners to save data in real-time
        genderPreferenceGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedGenderButton = view.findViewById(checkedId);
            registerInfoViewModel.getUserData().setGenderPreference(selectedGenderButton.getText().toString());
        });

        casualDatingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerInfoViewModel.getUserData().setIntentionCasualDating(isChecked);
        });

        longTermCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerInfoViewModel.getUserData().setIntentionLongTerm(isChecked);
        });

        marriageCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerInfoViewModel.getUserData().setIntentionMarriage(isChecked);
        });

        return view;
    }
}