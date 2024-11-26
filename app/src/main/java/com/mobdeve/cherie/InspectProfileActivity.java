package com.mobdeve.cherie;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class InspectProfileActivity extends AppCompatActivity {
    private TextView profileName;
    private TextView profileBio;
    private TextView profileHobby;
    private TextView profileHeight;
    private TextView profileGender;
    private TextView profileLocation;
    private TextView genderPreference;
    private TextView funFact;
    private TextView revealInfo;
    private TextView favoriteThings;
    private TextView profileIntention;

    String otherUserId;
    UserData otherUser;
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
        currentUserId = mAuth.getUid();

        // Initialize too many views
        profileName = findViewById(R.id.profileName);
        profileBio = findViewById(R.id.profileBio);
        profileHobby = findViewById(R.id.profileHobby);
        profileHeight = findViewById(R.id.profileHeight);
        profileGender = findViewById(R.id.profileGender);
        profileLocation = findViewById(R.id.profileLocation);
        genderPreference = findViewById(R.id.genderPreference);
        funFact = findViewById(R.id.funFact);
        revealInfo = findViewById(R.id.revealInfo);
        favoriteThings = findViewById(R.id.favoriteThings);
        profileIntention = findViewById(R.id.profileIntention);


        //Get intent Data
        this.otherUserId = getIntent().getStringExtra("id");

        //Get user data from Firestore
        dbRef.collection("users").document(otherUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                otherUser = task.getResult().toObject(UserData.class);

                profileName.setText(otherUser.getName());
                profileBio.setText(otherUser.getBio());
                profileHobby.setText(otherUser.getHobby());
                profileHeight.setText(String.valueOf(otherUser.getHeight()));
                profileGender.setText(otherUser.getGender());
                profileLocation.setText(otherUser.getLocation());
                genderPreference.setText(otherUser.getGenderPreference());

                //Show Dating Intention
                String intention = "";
                if (otherUser.isIntentionCasualDating())
                    intention += "Casual Dating, ";
                if (otherUser.isIntentionLongTerm())
                    intention += "Long Term, ";
                if (otherUser.isIntentionMarriage())
                    intention += "Marriage, ";

                if (intention.length() > 0)
                    profileIntention.setText(intention.substring(0, intention.length() - 2));
                else
                    profileIntention.setText("No intention specified");
                // Check message count
                checkMessageCount();
            }
        });

    }

    private void checkMessageCount() {
        dbRef.collection("chatrooms")
                .whereIn("userId1", Arrays.asList(currentUserId, otherUserId))
                .whereIn("userId2", Arrays.asList(currentUserId, otherUserId))
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String chatroomId = task.getResult().getDocuments().get(0).getId();
                        dbRef.collection("chatrooms").document(chatroomId)
                                .collection("messages").get().addOnCompleteListener(messageTask -> {
                                    if (messageTask.isSuccessful()) {
                                        int messageCount = messageTask.getResult().size();
                                        if (messageCount >= 50) {
                                            funFact.setText(otherUser.getFunFact());
                                            revealInfo.setText(otherUser.getRevealInfo());
                                            favoriteThings.setText(otherUser.getFavoriteThings());
                                        } else {
                                            funFact.setText("Unlock this user's charm data by chatting more!");
                                            revealInfo.setText("Unlock this user's charm data by chatting more!");
                                            favoriteThings.setText("Unlock this user's charm data by chatting more!");
                                            funFact.setTextColor(getResources().getColor(R.color.red));
                                            revealInfo.setTextColor(getResources().getColor(R.color.red));
                                            favoriteThings.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                });
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