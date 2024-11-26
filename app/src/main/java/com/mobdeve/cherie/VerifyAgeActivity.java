package com.mobdeve.cherie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerifyAgeActivity extends AppCompatActivity {
    private EditText ageField;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_age);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ageField = findViewById(R.id.ageField);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseFirestore.getInstance();
    }

    public void continueOnClick(View v) {
        String ageStr = ageField.getText().toString();

        if (ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Check if user is at least 18 years old
            if (age < 18) {
                dbRef.collection("users").document(userId).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mAuth.signOut();
                                                Toast.makeText(VerifyAgeActivity.this, "You must be at least 18 years old", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(VerifyAgeActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(VerifyAgeActivity.this, "Failed to delete user authentication", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(VerifyAgeActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return;
            }

            // Update user age
            dbRef.collection("users").document(userId)
                    .update("age", age)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(VerifyAgeActivity.this, RegisterInfoActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(VerifyAgeActivity.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void home(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}