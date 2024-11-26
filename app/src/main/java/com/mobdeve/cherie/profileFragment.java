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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class profileFragment extends Fragment {
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
    private Button editBtn;
    private Button saveBtn;
    private Button logoutBtn;

    private FirebaseFirestore dbRef;
    private FirebaseAuth currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize too many views
        profileName = view.findViewById(R.id.profileName);
        profileBio = view.findViewById(R.id.profileBio);
        profileHobby = view.findViewById(R.id.profileHobby);
        profileHeight = view.findViewById(R.id.profileHeight);
        profileGender = view.findViewById(R.id.profileGender);
        profileLocation = view.findViewById(R.id.profileLocation);
        genderPreference = view.findViewById(R.id.genderPreference);
        funFact = view.findViewById(R.id.funFact);
        revealInfo = view.findViewById(R.id.revealInfo);
        favoriteThings = view.findViewById(R.id.favoriteThings);
        profileIntention = view.findViewById(R.id.profileIntention);
        editBtn = view.findViewById(R.id.editBtn);
        saveBtn = view.findViewById(R.id.saveBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        dbRef = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();


        //display data from Firestore
        dbRef.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserData user = documentSnapshot.toObject(UserData.class);
                        if (user != null) {
                            profileName.setText(user.getName());
                            profileBio.setText(user.getBio());
                            profileHobby.setText(user.getHobby());
                            profileHeight.setText(String.valueOf(user.getHeight()));
                            profileGender.setText(user.getGender());
                            profileLocation.setText(user.getLocation());
                            genderPreference.setText(user.getGenderPreference());
                            funFact.setText(user.getFunFact());
                            revealInfo.setText(user.getRevealInfo());
                            favoriteThings.setText(user.getFavoriteThings());

                            //Show Dating Intention
                            String intention = "";
                            if(user.isIntentionCasualDating())
                                intention += "Casual Dating, ";
                            if(user.isIntentionLongTerm())
                                intention += "Long Term, ";
                            if(user.isIntentionMarriage())
                                intention += "Marriage, ";

                            profileIntention.setText(intention.substring(0, intention.length() - 2));
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
        // make textfields invisible

        // make edit textfields visible



        //Set button visiblity
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void saveProfile() {
        // make changes to Firestore

        // make textfields visible

        // make edit textfields invisible

        //Set button visiblity
        editBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
    }

    private void initializeData(){

    }
}