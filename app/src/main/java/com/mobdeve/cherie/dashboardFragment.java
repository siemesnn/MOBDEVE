package com.mobdeve.cherie;

import static android.app.Activity.RESULT_OK;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class dashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserData> listProfiles;
    private ProfileAdapter adapter;
    private FirebaseFirestore dbRef;
    private Button filterButton;
    private int minAge = 0, maxAge = 100;
    private String location = "Manila";
    private int currentProfileIndex = 0;
    private String currentUserId;

    private TextView noMoreTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.dashboardRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set LayoutManager
        recyclerView.setOnTouchListener((v, event) -> true);
        filterButton = view.findViewById(R.id.filterButton);
        dbRef = FirebaseFirestore.getInstance();
        listProfiles = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new ProfileAdapter(listProfiles);
        recyclerView.setAdapter(adapter);

        noMoreTextView = view.findViewById(R.id.noMoreText);
        noMoreTextView.setVisibility(View.GONE);

        view.findViewById(R.id.yesButton).setOnClickListener(v -> yesButton());
        view.findViewById(R.id.noButton).setOnClickListener(v -> noButton());

        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FilterActivity.class);
            startActivityForResult(intent, 1);
        });

        fetchProfiles();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            minAge = data.getIntExtra("minAge", 0);
            maxAge = data.getIntExtra("maxAge", 100);
            location = data.getStringExtra("location");
            fetchProfiles();
        }
    }

    public void yesButton(){
        //put logic here
        likeUser();
        showNextItem();
    }

    public void noButton(){
        //put logic here
        // mark that profile with regards to current user as no
        showNextItem();
    }

    private void checkProfiles() {
        if (listProfiles.isEmpty()) {
            noMoreTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noMoreTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showNextItem(){
        // this function will show the next item in the recycler view
        if (!listProfiles.isEmpty()){
            currentProfileIndex++;
            recyclerView.smoothScrollToPosition(currentProfileIndex);
        } else {
            noMoreTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //hide yes no button
            getView().findViewById(R.id.yesButton).setVisibility(View.GONE);
            getView().findViewById(R.id.noButton).setVisibility(View.GONE);
        }
    }

    //This is for testing purposes
    private void showPrevItem(){
        // this function will show the next item in the recycler view
        if (currentProfileIndex > 0){
            currentProfileIndex--;
            recyclerView.smoothScrollToPosition(currentProfileIndex);
        }
    }

    private void likeUser(){
        if (currentProfileIndex < listProfiles.size()) {
            UserData likedUser = listProfiles.get(currentProfileIndex);
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Retrieve the current user's name
            dbRef.collection("users").document(currentUserId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String currentUserName = documentSnapshot.getString("name");

                    // Add the liked user to the current user's "likedUsers" collection
                    dbRef.collection("users")
                            .document(currentUserId)
                            .collection("likedUsers")
                            .document(likedUser.getUserId())
                            .set(new HashMap<String, Object>() {{
                                put("name", likedUser.getName());
                            }})
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Liked " + likedUser.getName(), Toast.LENGTH_SHORT).show();

                                // Check if the liked user also liked the current user
                                dbRef.collection("users")
                                        .document(likedUser.getUserId())
                                        .collection("likedUsers")
                                        .document(currentUserId)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot1 -> {
                                            if (documentSnapshot1.exists()) {
                                                // Add each user to the other's matchedUsers collection
                                                dbRef.collection("users")
                                                        .document(currentUserId)
                                                        .collection("matchedUsers")
                                                        .document(likedUser.getUserId())
                                                        .set(new HashMap<String, Object>() {{
                                                            put("name", likedUser.getName());
                                                        }})
                                                        .addOnSuccessListener(aVoid1 -> {
                                                            dbRef.collection("users")
                                                                    .document(likedUser.getUserId())
                                                                    .collection("matchedUsers")
                                                                    .document(currentUserId)
                                                                    .set(new HashMap<String, Object>() {{
                                                                        put("name", currentUserName); // Use the current user's name
                                                                    }})
                                                                    .addOnSuccessListener(aVoid2 -> {
                                                                        createChatroom(currentUserId, likedUser.getUserId());
                                                                        Toast.makeText(getContext(), "It's a match with " + likedUser.getName(), Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> e.printStackTrace());
                                                        })
                                                        .addOnFailureListener(e -> e.printStackTrace());
                                            } else {
                                                Toast.makeText(getContext(), "Liked " + likedUser.getName(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> e.printStackTrace());
                            })
                            .addOnFailureListener(e -> e.printStackTrace());
                }
            }).addOnFailureListener(e -> e.printStackTrace());
        }
    }

    private void createChatroom(String userId1, String userId2) {
        Map<String, Object> chatroomData = new HashMap<>();
        chatroomData.put("userId1", userId1);
        chatroomData.put("userId2", userId2);

        dbRef.collection("chatrooms")
                .add(chatroomData)
                .addOnSuccessListener(documentReference -> {
//                    Log.d("dashboardFragment", "Chatroom created with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
//                    Log.w("dashboardFragment", "Error creating chatroom", e);
                });
    }

    public void checkProfileListOnStart(){
        if (listProfiles.size() == 0){
            noMoreTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.yesButton).setVisibility(View.GONE);
            getView().findViewById(R.id.noButton).setVisibility(View.GONE);
        } else {
            noMoreTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchProfiles() {
        dbRef.collection("users")
                .document(currentUserId)
                .collection("likedUsers")
                .get()
                .addOnSuccessListener(likedUsersSnapshot -> {
                    List<String> likedUserIds = new ArrayList<>();
                    for (QueryDocumentSnapshot document : likedUsersSnapshot) {
                        likedUserIds.add(document.getId());
                    }
                    // Add the current user's ID to the list to filter out self
                    likedUserIds.add(currentUserId);

                    // Fetch all profiles excluding liked profiles and self
                    dbRef.collection("users")
                            .whereGreaterThanOrEqualTo("age", minAge)
                            .whereLessThanOrEqualTo("age", maxAge)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                listProfiles.clear();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    UserData profile = documentSnapshot.toObject(UserData.class);
                                    if (profile != null) {
                                        String profileLocation = profile.getLocation();
                                        if ((location.isEmpty() || (profileLocation != null && profileLocation.equals(location))) &&
                                                !likedUserIds.contains(profile.getUserId())) {
                                            listProfiles.add(profile);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                checkProfiles();
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                Log.e("fetchProfiles", "Error fetching profiles", e);
                            });
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
}