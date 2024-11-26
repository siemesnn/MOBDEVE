package com.mobdeve.cherie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class matchesFragment extends Fragment {
    private RecyclerView recyclerViewLike;
    private RecyclerView recyclerViewMatch;
    private SentInvitesAdapter likeAdapter;
    private MatchScrollAdapter matchAdapter;

    private FirebaseFirestore dbRef;
    private String currentUserId;
    private ArrayList<UserData> likedUsers;
    private ArrayList<UserData> matchedUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        // This is recycler view for liked users
        recyclerViewLike = view.findViewById(R.id.likedRecyclerViews);
        recyclerViewLike.setHasFixedSize(true);
        recyclerViewLike.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewMatch = view.findViewById(R.id.matchScrollRecyclerviews);
        recyclerViewMatch.setHasFixedSize(true);
        recyclerViewMatch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        likedUsers = new ArrayList<>();
        matchedUsers = new ArrayList<>();
        dbRef = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();

        fetchMatches();

        likeAdapter = new SentInvitesAdapter(likedUsers);
        recyclerViewLike.setAdapter(likeAdapter);

        matchAdapter = new MatchScrollAdapter(matchedUsers);
        recyclerViewMatch.setAdapter(matchAdapter);

        return view;
    }

    private void fetchMatches() {
        dbRef.collection("users").document(currentUserId).collection("matchedUsers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String matchId = document.getId();
                            dbRef.collection("users").document(matchId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            UserData match = task1.getResult().toObject(UserData.class);
                                            matchedUsers.add(match);
                                            matchAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                    fetchLikes(); // Call fetchLikes after fetching matched users
                });
    }

    private void fetchLikes() {
        dbRef.collection("users").document(currentUserId).collection("likedUsers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<UserData> allLikedUsers = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String likedUserId = document.getId();
                            dbRef.collection("users").document(likedUserId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            UserData likedUser = task1.getResult().toObject(UserData.class);
                                            allLikedUsers.add(likedUser);
                                            if (allLikedUsers.size() == task.getResult().size()) {
                                                filterLikedUsers(allLikedUsers);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void filterLikedUsers(ArrayList<UserData> allLikedUsers) {
        Set<String> matchedUserIds = new HashSet<>();
        for (UserData user : matchedUsers) {
            matchedUserIds.add(user.getUserId());
        }

        for (UserData likedUser : allLikedUsers) {
            if (!matchedUserIds.contains(likedUser.getUserId())) {
                likedUsers.add(likedUser);
            }
        }
        likeAdapter.notifyDataSetChanged();
    }
}