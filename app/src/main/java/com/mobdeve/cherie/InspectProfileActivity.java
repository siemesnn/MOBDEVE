package com.mobdeve.cherie;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class InspectProfileActivity extends AppCompatActivity {
    TextView NameView;
    TextView BioView;
    TextView HobbyView;

    String otherUserId;
    userData otherUser;
    String currentUserId;

    private FirebaseFirestore dbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inspect_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        NameView = findViewById(R.id.profileName);
        BioView = findViewById(R.id.profileBio);
        HobbyView = findViewById(R.id.profileHobby);

        //Get intent Data
        this.otherUserId = getIntent().getStringExtra("id");

        //Get user data from Firestore
        dbRef.collection("users").document(otherUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                otherUser = task.getResult().toObject(userData.class);
                NameView.setText(otherUser.getName());
                BioView.setText(otherUser.getBio());
                HobbyView.setText(otherUser.getHobby());
            }
        });

    }

    public void unmatch(View view) {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Remove other user from current user's liked list
        dbRef.collection("users").document(currentUserId)
                .collection("likedUsers").document(otherUserId).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Remove current user from other user's liked list
                        dbRef.collection("users").document(otherUserId)
                                .collection("likedUsers").document(currentUserId).delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Remove other user from current user's matches list
                                        dbRef.collection("users").document(currentUserId)
                                                .collection("matchedUsers").document(otherUserId).delete()
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        // Remove current user from other user's matches list
                                                        dbRef.collection("users").document(otherUserId)
                                                                .collection("matchedUsers").document(currentUserId).delete()
                                                                .addOnCompleteListener(task3 -> {
                                                                    if (task3.isSuccessful()) {
                                                                        // Go back to previous activity
                                                                        // In the previous activity or fragment
                                                                        finish();
                                                                    }});
                                                    }});
                                    }});
                    }});
    }

    public void startGame(){

    }


}