package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Button saveBtn;
        Button editBtn;

        ImageButton home;
        ImageButton chat;
        ImageButton settings;



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        home = findViewById(R.id.home_button);
        chat = findViewById(R.id.chat_button);
        settings = findViewById(R.id.settings_button);

        editBtn=findViewById(R.id.editBtn);
        saveBtn=findViewById(R.id.saveBtn);
    }

    public void editProfile(View v){
        EditText profileName;
        EditText profileBio;
        Button editBtn;
        Button saveBtn;
        profileName=findViewById(R.id.profileName);
        profileBio=findViewById(R.id.profileBio);
        editBtn=findViewById(R.id.editBtn);
        saveBtn=findViewById(R.id.saveBtn);


        profileName.setFocusableInTouchMode(true);
        profileBio.setFocusableInTouchMode(true);
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);

    }

    public void saveProfile(View v){
        EditText profileName;
        EditText profileBio;
        Button editBtn;
        Button saveBtn;
        profileName=findViewById(R.id.profileName);
        profileBio=findViewById(R.id.profileBio);
        editBtn=findViewById(R.id.editBtn);
        saveBtn=findViewById(R.id.saveBtn);

        profileName.setFocusableInTouchMode(false);
        profileBio.setFocusableInTouchMode(false);
        editBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
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
}
