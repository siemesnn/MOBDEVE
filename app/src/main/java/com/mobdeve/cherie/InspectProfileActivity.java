package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCaller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private TextView compatibilityRating;
    private Button gameButton;

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

        compatibilityRating = findViewById(R.id.compatibilityRating);
        gameButton = findViewById(R.id.gameButton);

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

                checkCompatibility();
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

    public void startGame(View view){
        Intent i = new Intent(InspectProfileActivity.this, GameshowActivity.class);
        startActivityForResult(i, 1);
    }

    private void checkCompatibility(){
        dbRef.collection("chatrooms")
                .whereIn("userId1", Arrays.asList(currentUserId, otherUserId))
                .whereIn("userId2", Arrays.asList(currentUserId, otherUserId))
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String chatroomId = task.getResult().getDocuments().get(0).getId();
                        dbRef.collection("chatrooms").document(chatroomId)
                                .collection("gameshow").get().addOnCompleteListener(gameTask -> {
                                    if (gameTask.isSuccessful()) {
                                        boolean currentUserPlayed = false;
                                        boolean otherUserPlayed = false;
                                        int user1Percentage = 0;
                                        int user2Percentage = 0;

                                        // single out the results if both users have played the game
                                        for (DocumentSnapshot document : gameTask.getResult().getDocuments()) {
                                            String userId = document.getString("userId");
                                            int percentage = document.getLong("percentage").intValue();
                                            if (userId.equals(currentUserId)) {
                                                currentUserPlayed = true;
                                                user1Percentage = percentage;
                                            } else if (userId.equals(otherUserId)) {
                                                otherUserPlayed = true;
                                                user2Percentage = percentage;
                                            }
                                        }

                                        // if both users have played the game, calculate compatibility
                                        if (currentUserPlayed && otherUserPlayed) {
                                            int compatibility = Math.abs(user1Percentage - user2Percentage);
                                            compatibility = 100 - compatibility;
                                            compatibilityRating.setText("Compatibility Rating: " + compatibility + "%");
                                            compatibilityRating.setVisibility(View.VISIBLE);
                                            gameButton.setVisibility(View.GONE);
                                        }
                                        // if only current played the game
                                        else if (currentUserPlayed) {
                                            compatibilityRating.setText("Compatibility Rating: Waiting for other user");
                                            compatibilityRating.setVisibility(View.VISIBLE);
                                            gameButton.setVisibility(View.GONE);
                                        }
                                        //if only other or neither played the game
                                        else {
                                            compatibilityRating.setVisibility(View.GONE);
                                            gameButton.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            int percentage = data.getIntExtra("percentage", 0);

            // send data to firestore users' chatroom id on gameshow collection

            // if other user has not played the game, show message
            // "Compatibility Rating: Waiting for other user"

            // if other user has also played the game, calculate compatibility, display on both
            dbRef.collection("chatrooms")
                    .whereIn("userId1", Arrays.asList(currentUserId, otherUserId))
                    .whereIn("userId2", Arrays.asList(currentUserId, otherUserId))
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String chatroomId = task.getResult().getDocuments().get(0).getId();
                            dbRef.collection("chatrooms").document(chatroomId)
                                    .collection("gameshow")
                                    .document(currentUserId)
                                    .set(new GameResultData(currentUserId, percentage))
                                    .addOnCompleteListener(gameTask -> {
                                        if (gameTask.isSuccessful()) {
                                            // Check compatibility after updating the result
                                            checkCompatibility();
                                        }
                                    });
                        }
                    });

            compatibilityRating.setVisibility(View.VISIBLE);
            gameButton.setVisibility(View.GONE);
        }
    }



}