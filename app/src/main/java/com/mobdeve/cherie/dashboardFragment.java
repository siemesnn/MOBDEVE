package com.mobdeve.cherie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import javax.annotation.Nullable;


public class dashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<userData> listProfiles;
    private FirebaseFirestore dbRef;
    private int currentProfileIndex = 0;
    private String currentUserId;

    private TextView noMoreTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.dashboardRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setOnTouchListener((v, event) -> true);
        //call items
        listProfiles = new ArrayList<>();
        dbRef = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchProfiles();

        //set adapter
        adapter = new ProfileAdapter(listProfiles);
        recyclerView.setAdapter(adapter);

        noMoreTextView = view.findViewById(R.id.noMoreText);
        noMoreTextView.setVisibility(View.GONE);

        // If there is no profile on load, notify user there is no more profiles
        view.findViewById(R.id.yesButton).setOnClickListener(v -> yesButton());
        view.findViewById(R.id.noButton).setOnClickListener(v -> noButton());
        return view;
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

    private void showNextItem(){
        // this function will show the next item in the recycler view
        if (currentProfileIndex < listProfiles.size() - 1){
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
        // this function will mark that the current user liked the current profile
        userData likedUser = listProfiles.get(currentProfileIndex);
        // add the liked user to the current user's "likedUsers" collection
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
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
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
                                                            put("name", currentUserId);
                                                        }})
                                                        .addOnSuccessListener(aVoid2 -> {
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
                .addOnFailureListener(e -> e.printStackTrace());// TODO: Check if the current user is also liked by the liked user, if so, create a match

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

    private void fetchProfiles(){

        // Get the current user's liked profiles
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
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                listProfiles.clear();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    userData profile = documentSnapshot.toObject(userData.class);
                                    if (!likedUserIds.contains(profile.getUserId())) {
                                        listProfiles.add(profile);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                checkProfileListOnStart();
                            })
                            .addOnFailureListener(e -> e.printStackTrace());
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
}