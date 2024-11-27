package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    private EditText minAgeEditText, maxAgeEditText;
    private Spinner locationSpinner;
    private Button applyFilterButton;
    private FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        minAgeEditText = findViewById(R.id.minAgeEditText);
        maxAgeEditText = findViewById(R.id.maxAgeEditText);
        locationSpinner = findViewById(R.id.locationSpinner);
        applyFilterButton = findViewById(R.id.applyFilterButton);
        dbRef = FirebaseFirestore.getInstance();

        loadLocations();

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minAge = Integer.parseInt(minAgeEditText.getText().toString());
                int maxAge = Integer.parseInt(maxAgeEditText.getText().toString());
                String location = locationSpinner.getSelectedItem().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("minAge", minAge);
                resultIntent.putExtra("maxAge", maxAge);
                resultIntent.putExtra("location", location);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void loadLocations() {
        dbRef.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> locations = new ArrayList<>();
                locations.add("None");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String location = document.getString("location");
                    if (location != null && !locations.contains(location)) {
                        locations.add(location);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(adapter);
            } else {
                task.getException().printStackTrace();
            }
        });
    }
}