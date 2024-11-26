package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class profileFragment extends Fragment {
    // Text views
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

    //Editing fields
    private EditText editName;
    private EditText editBio;
    private EditText editHobby;
    private EditText editHeight;
    private RadioGroup editGender;
    private Spinner profileLocationSpinner;
    private RadioGroup editGenderPreference;
    private EditText editFunFact;
    private EditText editRevealInfo;
    private EditText editFavoriteThings;
    private LinearLayout intentionGroup;
    private CheckBox intentionCasualDating;
    private CheckBox intentionLongTerm;
    private CheckBox intentionMarriage;

    //Buttons
    private Button editBtn;
    private Button saveBtn;
    private Button logoutBtn;

    //Firebase
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

        // initialize edit views T-T
        editName = view.findViewById(R.id.profileNameEdit);
        editBio = view.findViewById(R.id.profileBioEdit);
        editHobby = view.findViewById(R.id.profileHobbyEdit);
        editHeight = view.findViewById(R.id.profileHeightEdit);
        editGender = view.findViewById(R.id.genderGroup);
        profileLocationSpinner = view.findViewById(R.id.profileLocationSpinner);
        editGenderPreference = view.findViewById(R.id.genderPreferenceGroup);
        editFunFact = view.findViewById(R.id.funFactEdit);
        editRevealInfo = view.findViewById(R.id.revealInfoEdit);
        editFavoriteThings = view.findViewById(R.id.favoriteThingsEdit);
        intentionGroup = view.findViewById(R.id.intentionGroup);
        intentionCasualDating = view.findViewById(R.id.intentionCasualDating);
        intentionLongTerm = view.findViewById(R.id.intentionLongTerm);
        intentionMarriage = view.findViewById(R.id.intentionMarriage);

        // List of cities in Manila
        String[] citiesInManila = {"Manila", "Quezon City", "Caloocan", "Makati", "Pasig", "Taguig", "Mandaluyong", "Marikina", "Parañaque", "Las Piñas"};

        // Set up the adapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, citiesInManila);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileLocationSpinner.setAdapter(adapter);

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

                            if(intention.length() > 0)
                                profileIntention.setText(intention.substring(0, intention.length() - 2));
                            else
                                profileIntention.setText("No intention specified");
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
        // pass value
        // Populate EditTexts with current data
        editName.setText(profileName.getText().toString());
        editBio.setText(profileBio.getText().toString());
        editHobby.setText(profileHobby.getText().toString());
        editHeight.setText(profileHeight.getText().toString());
        editFunFact.setText(funFact.getText().toString());
        editRevealInfo.setText(revealInfo.getText().toString());
        editFavoriteThings.setText(favoriteThings.getText().toString());

        String currentLocation = profileLocation.getText().toString();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) profileLocationSpinner.getAdapter();
        int spinnerPosition = adapter.getPosition(currentLocation);
        profileLocationSpinner.setSelection(spinnerPosition);


        String gender = profileGender.getText().toString();
        if (gender.equals("Male")) {
            editGender.check(R.id.genderMale);
        } else if (gender.equals("Female")) {
            editGender.check(R.id.genderFemale);
        } else {
            editGender.check(R.id.genderOther);
        }

        // genderPref
        String genderPref = genderPreference.getText().toString();
        if (genderPref.equals("Male")) {
            editGenderPreference.check(R.id.preferenceMale);
        } else if (genderPref.equals("Female")) {
            editGenderPreference.check(R.id.preferenceFemale);
        } else {
            editGenderPreference.check(R.id.preferenceOther);
        }

        // Set the selected checkboxes based on the current dating intention
        String intention = profileIntention.getText().toString();
        intentionCasualDating.setChecked(intention.contains("Casual Dating"));
        intentionLongTerm.setChecked(intention.contains("Long Term"));
        intentionMarriage.setChecked(intention.contains("Marriage"));


        // make textfields invisible
        profileName.setVisibility(View.GONE);
        profileBio.setVisibility(View.GONE);
        profileHobby.setVisibility(View.GONE);
        profileHeight.setVisibility(View.GONE);
        profileGender.setVisibility(View.GONE);
        profileLocation.setVisibility(View.GONE);
        genderPreference.setVisibility(View.GONE);
        funFact.setVisibility(View.GONE);
        revealInfo.setVisibility(View.GONE);
        favoriteThings.setVisibility(View.GONE);
        profileIntention.setVisibility(View.GONE);

        // make input fields visible
        editName.setVisibility(View.VISIBLE);
        editBio.setVisibility(View.VISIBLE);
        editHobby.setVisibility(View.VISIBLE);
        editHeight.setVisibility(View.VISIBLE);
        editGender.setVisibility(View.VISIBLE);
        profileLocationSpinner.setVisibility(View.VISIBLE);
        editGenderPreference.setVisibility(View.VISIBLE);
        editFunFact.setVisibility(View.VISIBLE);
        editRevealInfo.setVisibility(View.VISIBLE);
        editFavoriteThings.setVisibility(View.VISIBLE);
        intentionGroup.setVisibility(View.VISIBLE);


        // make edit textfields visible



        //Set button visiblity
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void saveProfile() {
        // pass values to textviews
        profileName.setText(editName.getText().toString());
        profileBio.setText(editBio.getText().toString());
        profileHobby.setText(editHobby.getText().toString());
        profileHeight.setText(editHeight.getText().toString());
        profileLocation.setText(profileLocationSpinner.getSelectedItem().toString());
        funFact.setText(editFunFact.getText().toString());
        revealInfo.setText(editRevealInfo.getText().toString());
        favoriteThings.setText(editFavoriteThings.getText().toString());

        // gender
        int selectedGenderId = editGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.genderMale)
            profileGender.setText("Male");
        else if (selectedGenderId == R.id.genderFemale)
            profileGender.setText("Female");
        else
            profileGender.setText("Other");


        // genderPref
        int selectedGenderPrefId = editGenderPreference.getCheckedRadioButtonId();
        if (selectedGenderPrefId == R.id.preferenceMale)
            genderPreference.setText("Male");
        else if (selectedGenderPrefId == R.id.preferenceFemale)
            genderPreference.setText("Female");
        else
            genderPreference.setText("Both");

        // datingIntention
        StringBuilder intention = new StringBuilder();
        if (intentionCasualDating.isChecked())
            intention.append("Casual Dating, ");
        if (intentionLongTerm.isChecked())
            intention.append("Long Term, ");
        if (intentionMarriage.isChecked())
            intention.append("Marriage, ");

        if(intention.length() > 0)
            profileIntention.setText(intention.substring(0, intention.length() - 2));
        else
            profileIntention.setText("No intention specified");

        // make textfields visible
        profileName.setVisibility(View.VISIBLE);
        profileBio.setVisibility(View.VISIBLE);
        profileHobby.setVisibility(View.VISIBLE);
        profileHeight.setVisibility(View.VISIBLE);
        profileGender.setVisibility(View.VISIBLE);
        profileLocation.setVisibility(View.VISIBLE);
        genderPreference.setVisibility(View.VISIBLE);
        funFact.setVisibility(View.VISIBLE);
        revealInfo.setVisibility(View.VISIBLE);
        favoriteThings.setVisibility(View.VISIBLE);
        profileIntention.setVisibility(View.VISIBLE);

        // make input fields invisible
        editName.setVisibility(View.GONE);
        editBio.setVisibility(View.GONE);
        editHobby.setVisibility(View.GONE);
        editHeight.setVisibility(View.GONE);
        editGender.setVisibility(View.GONE);
        profileLocationSpinner.setVisibility(View.GONE);
        editGenderPreference.setVisibility(View.GONE);
        editFunFact.setVisibility(View.GONE);
        editRevealInfo.setVisibility(View.GONE);
        editFavoriteThings.setVisibility(View.GONE);
        intentionGroup.setVisibility(View.GONE);

        //Set button visiblity
        editBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);

        updateFirebase();
    }

    private void updateFirebase() {
        // Create a map to store the updated values
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("name", editName.getText().toString());
        updatedValues.put("bio", editBio.getText().toString());
        updatedValues.put("hobby", editHobby.getText().toString());
        updatedValues.put("height", Float.parseFloat(editHeight.getText().toString()));
        updatedValues.put("gender", profileGender.getText().toString());
        updatedValues.put("location", profileLocationSpinner.getSelectedItem().toString());
        updatedValues.put("genderPreference", genderPreference.getText().toString());
        updatedValues.put("funFact", editFunFact.getText().toString());
        updatedValues.put("revealInfo", editRevealInfo.getText().toString());
        updatedValues.put("favoriteThings", editFavoriteThings.getText().toString());

        // Update dating intentions
        updatedValues.put("intentionCasualDating", intentionCasualDating.isChecked());
        updatedValues.put("intentionLongTerm", intentionLongTerm.isChecked());
        updatedValues.put("intentionMarriage", intentionMarriage.isChecked());

        // Get the current user ID
        String userId = currentUser.getUid();

        // Update the Firestore document
        dbRef.collection("users").document(userId)
                .update(updatedValues)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    // Optionally, show a message to the user
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    e.printStackTrace();
                    // Optionally, show an error message to the user
                });
    }
}