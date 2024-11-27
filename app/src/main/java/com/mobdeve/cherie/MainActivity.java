package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseMessaging fcm;
    FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();



                        // Log and toast
                        System.out.println("FCM token: " + token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        // Save the token to Firestore
                        if (user != null) {

                            dbRef = FirebaseFirestore.getInstance();
                            dbRef.collection("users").document(user.getUid())
                                    .update("fcmToken", token)
                                    .addOnSuccessListener(aVoid -> System.out.println("FCM token saved to Firestore"))
                                    .addOnFailureListener(e -> System.out.println("Error saving FCM token to Firestore"));
                        }
                    }
                });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null) {
            Intent i = new Intent(getApplicationContext(), dashboard.class);
            startActivity(i);
            finish();
        }

    }
        public void loginPage(View v){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        public void registerPage(View v){
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        }

}