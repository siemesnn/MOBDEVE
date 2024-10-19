package com.mobdeve.cherie.registered;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.mobdeve.cherie.R;
import com.mobdeve.cherie.dashboard;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class  registered extends AppCompatActivity {

    FrameLayout fragmentContainer;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registered);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        tabLayout = findViewById(R.id.tabLayout);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new character()).addToBackStack(null).commit();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                 Fragment fragment = null;

                switch(tab.getPosition()){
                    case 0:
                        fragment = new character();
                        break;
                    case 1:
                        fragment = new charm();
                        break;
                    case 2:
                        fragment = new choice();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    public void next(View view) {
        Intent i = new Intent(registered.this, dashboard.class);
        startActivity(i);
    }
}