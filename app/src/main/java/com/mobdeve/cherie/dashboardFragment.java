package com.mobdeve.cherie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class dashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<userData> listProfiles;
    private FirebaseFirestore dbRef;
    private int currentProfileIndex = 0;

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
        fetchProfiles();

        //set adapter
        adapter = new ProfileAdapter(listProfiles);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.yesButton).setOnClickListener(v -> yesButton());
        view.findViewById(R.id.noButton).setOnClickListener(v -> noButton());
        return view;
    }

    public void yesButton(){
        //put logic here
        // mark that profile with regards to current user as yes
        showNextItem();
    }

    public void noButton(){
        //put logic here
        // mark that profile with regards to current user as no
        showPrevItem();
    }

    private void showNextItem(){
        // this function will show the next item in the recycler view
        if (currentProfileIndex < listProfiles.size() - 1){
            currentProfileIndex++;
            recyclerView.smoothScrollToPosition(currentProfileIndex);
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

    private void fetchProfiles(){
        dbRef.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        userData profile = documentSnapshot.toObject(userData.class);
                        listProfiles.add(profile);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
}