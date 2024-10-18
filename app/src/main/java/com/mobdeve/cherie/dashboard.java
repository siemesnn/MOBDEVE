package com.mobdeve.cherie;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dashboard extends AppCompatActivity {

    ImageButton home;
    ImageButton chat;
    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        home = findViewById(R.id.home_button);
        chat = findViewById(R.id.chat_button);
        settings = findViewById(R.id.settings_button);

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
        Intent i = new Intent(this, settings.class);
        startActivity(i);
    }

    public void matchesNav(View v){
        Intent i = new Intent(this, matches.class);
        startActivity(i);
    }
}