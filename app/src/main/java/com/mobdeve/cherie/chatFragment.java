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


public class chatFragment extends Fragment {
    private RecyclerView recyclerViewChat;
    private RecyclerView recyclerViewMatch;
    private ChatAdapter chatAdapter;
    private MatchAdapter matchAdapter;
    private FirebaseFirestore dbRef;
    private String currentUserId;
    private ArrayList<userData> listMatches;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // This is recycler view for inbox
        recyclerViewChat = view.findViewById(R.id.chatRecyclerView);
        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));

        //This is recycler view for matches
        recyclerViewMatch = view.findViewById(R.id.matchRecyclerView);
        recyclerViewMatch.setHasFixedSize(true);
        recyclerViewMatch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listMatches = new ArrayList<>();

        dbRef = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchMatches();

        matchAdapter = new MatchAdapter(listMatches);
        recyclerViewMatch.setAdapter(matchAdapter);

        chatAdapter = new ChatAdapter(listMatches);
        recyclerViewChat.setAdapter(chatAdapter);

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
                                            userData match = task1.getResult().toObject(userData.class);
                                            listMatches.add(match);
                                            chatAdapter.notifyDataSetChanged();
                                            matchAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });
    }
}