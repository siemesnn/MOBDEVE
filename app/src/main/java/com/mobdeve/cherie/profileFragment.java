package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class profileFragment extends Fragment {
    private EditText profileName;
    private EditText profileBio;
    private TextView profileHobby;
    private Button editBtn;
    private Button saveBtn;
    private Button logoutBtn;

    private FirebaseFirestore dbRef;
    private FirebaseAuth currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileBio = view.findViewById(R.id.profileBio);
        profileHobby = view.findViewById(R.id.profileHobby);
        editBtn = view.findViewById(R.id.editBtn);
        saveBtn = view.findViewById(R.id.saveBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        dbRef = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();

        dbRef.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userData user = documentSnapshot.toObject(userData.class);
                        if (user != null) {
                            profileName.setText(user.getName());
                            profileBio.setText(user.getBio());
                            profileHobby.setText(user.getHobby());
                        }
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());

        editBtn.setOnClickListener(v -> editProfile());
        saveBtn.setOnClickListener(v -> saveProfile());
        logoutBtn.setOnClickListener(v -> logout(v));

        return view;
    }

    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private void editProfile() {
        profileName.setFocusableInTouchMode(true);
        profileBio.setFocusableInTouchMode(true);
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void saveProfile() {
        profileName.setFocusableInTouchMode(false);
        profileBio.setFocusableInTouchMode(false);
        editBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
    }
}