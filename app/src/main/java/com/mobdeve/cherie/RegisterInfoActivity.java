package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterInfoActivity extends AppCompatActivity {

//    private ProgressBar progressBar;
//    private FrameLayout contentFrame; // To hold different content for each step
//    private int currentStep = 0; // Track the current step

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private RegisterInfoViewModel registerInfoViewModel;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registered_info);
        // Setting up window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        registerInfoViewModel = new ViewModelProvider(this).get(RegisterInfoViewModel.class);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.TabLayout);

        viewPager.setAdapter(new SectionsPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Character");
                            break;
                        case 1:
                            tab.setText("Choice");
                            break;
                        case 2:
                            tab.setText("Charm");
                            break;
                    }
                }).attach();
    }



    public void submitInfo(View v){
        UserData userData = registerInfoViewModel.getUserData();

        // For reference
        //Character: bio, hobby, height, gender, location
        //Choice: genderPreference, intentionCasualDating, intentionLongTerm, intentionMarriage
        //Charm: funFact, revealInfo, favoriteThings

        // Send all that data to Firestore
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("bio", userData.getBio());
            userUpdates.put("hobby", userData.getHobby());
            userUpdates.put("height", userData.getHeight());
            userUpdates.put("gender", userData.getGender());
            userUpdates.put("location", userData.getLocation());
            userUpdates.put("genderPreference", userData.getGenderPreference());
            userUpdates.put("intentionCasualDating", userData.isIntentionCasualDating());
            userUpdates.put("intentionLongTerm", userData.isIntentionLongTerm());
            userUpdates.put("intentionMarriage", userData.isIntentionMarriage());
            userUpdates.put("funFact", userData.getFunFact());
            userUpdates.put("revealInfo", userData.getRevealInfo());
            userUpdates.put("favoriteThings", userData.getFavoriteThings());

            db.collection("users").document(user.getUid())
                    .update(userUpdates)
                    .addOnSuccessListener(aVoid -> Log.d("SubmitInfo", "User data updated in Firestore."))
                    .addOnFailureListener(e -> Log.w("SubmitInfo", "Error updating user data", e));

            Intent i = new Intent(RegisterInfoActivity.this, dashboard.class);
            startActivity(i);
            finish();
        }


    }

    private static class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CharacterFragment();
                case 1:
                    return new ChoiceFragment();
                case 2:
                    return new CharmFragment();
                default:
                    return new CharacterFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
