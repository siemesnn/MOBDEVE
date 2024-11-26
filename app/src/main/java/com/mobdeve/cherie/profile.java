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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {
    EditText profileName;
    EditText profileBio;
    TextView profileHobby;
    Button editBtn;
    Button saveBtn;
    ImageButton home;
    ImageButton chat;
    ImageButton settings;

    private FirebaseFirestore dbRef;
    private FirebaseAuth currentUser;

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

        home = findViewById(R.id.home_button);
        chat = findViewById(R.id.chat_button);
        settings = findViewById(R.id.settings_button);

        profileName=findViewById(R.id.profileName);
        profileBio=findViewById(R.id.profileBio);
        profileHobby=findViewById(R.id.profileHobby);
        editBtn=findViewById(R.id.editBtn);
        saveBtn=findViewById(R.id.saveBtn);

        // get firestore db collection's user collections then get name and bio
        dbRef = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();

        dbRef.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserData user = documentSnapshot.toObject(UserData.class);
                        // Set the retrieved data to the EditText fields
                        if(user != null){
                            profileName.setText(user.getName());
                            profileBio.setText(user.getBio());
                            profileHobby.setText(user.getHobby());
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    e.printStackTrace();
                });

    }

    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void editProfile(View v){

        profileName.setFocusableInTouchMode(true);
        profileBio.setFocusableInTouchMode(true);
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);

    }

    public void saveProfile(View v){

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
