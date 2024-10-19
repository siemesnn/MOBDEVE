package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class profile extends AppCompatActivity {

    private Spinner preferencesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        ImageButton home = findViewById(R.id.home_button);
        ImageButton chat = findViewById(R.id.chat_button);
        ImageButton settings = findViewById(R.id.settings_button);
        ImageButton editPFP = findViewById(R.id.edit_pfp);
        Button editBtn = findViewById(R.id.editBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        Chip addBtn = findViewById(R.id.addHobby);

        // Initialize spinner
        preferencesSpinner = findViewById(R.id.preferencesSpinner);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferencesSpinner.setAdapter(adapter);

        preferencesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPreference = parent.getItemAtPosition(position).toString();
                // Handle the selected preference
                Toast.makeText(parent.getContext(), "Selected: " + selectedPreference, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected if needed
            }
        });
    }

    public void editProfile(View v){
        EditText profileName = findViewById(R.id.profileName);
        EditText profileBio = findViewById(R.id.profileBio);
        Button editBtn = findViewById(R.id.editBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        Chip addBtn = findViewById(R.id.addHobby);
        ImageButton editPFP = findViewById(R.id.edit_pfp);

        profileName.setFocusableInTouchMode(true);
        profileBio.setFocusableInTouchMode(true);
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
        addBtn.setVisibility(View.VISIBLE);
        editPFP.setVisibility(View.VISIBLE);
    }

    public void saveProfile(View v){
        EditText profileName = findViewById(R.id.profileName);
        EditText profileBio = findViewById(R.id.profileBio);
        Button editBtn = findViewById(R.id.editBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        Chip addBtn = findViewById(R.id.addHobby);
        ImageButton editPFP = findViewById(R.id.edit_pfp);

        profileName.setFocusableInTouchMode(false);
        profileBio.setFocusableInTouchMode(false);
        editBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);
        editPFP.setVisibility(View.GONE);
    }

    public void dashboardNav(View v){
        Intent i = new Intent(this, dashboard.class);
        startActivity(i);
    }

    public void chatNav(View v){
        Intent i = new Intent(this, chat.class);
        startActivity(i);
    }

    public void settingsNav(View v){
        Intent i = new Intent(this, profile.class);
        startActivity(i);
    }

    public void matchesNav(View v){
        Intent i = new Intent(this, matches.class);
        startActivity(i);
    }

    public void hobbies(View v){
        Intent i = new Intent(this, hobbies.class);
        startActivity(i);
    }
}

