package com.mobdeve.cherie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ChatroomActivity extends AppCompatActivity {
    // Views needed
    private TextView welcomeTv;
    private EditText messageEtv;
    private Button sendBtn;

    // RecyclerView Components
    private RecyclerView recyclerView;
    // Replacement of the base adapter view
    private messageAdapter myFirestoreRecyclerAdapter;

    // DB reference
    private FirebaseFirestore dbRef;
    private String chatroomId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        this.welcomeTv = findViewById(R.id.welcomeTv);
        this.messageEtv = findViewById(R.id.messageEtv);
        this.sendBtn = findViewById(R.id.sendBtn);
        this.recyclerView = findViewById(R.id.chatRecyclerView);

        // Get intents
        this.chatroomId = getIntent().getStringExtra("chatroomId");

        // Initialize Firebase things
        dbRef = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef.collection("users").document(currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userData currentUserData = task.getResult().toObject(userData.class);
                        this.username = currentUserData.getName();
                        this.welcomeTv.setText("Welcome, " + username + "!");

                        // Get the messages from the Message Collection within the specific chatroom
                        Query query = dbRef
                                .collection("chatrooms")
                                .document(chatroomId)
                                .collection("messages")
                                .orderBy("timestamp");

                        // Define options for FirestoreRecyclerAdapter
                        FirestoreRecyclerOptions<messageData> options = new FirestoreRecyclerOptions.Builder<messageData>()
                                .setQuery(query, messageData.class)
                                .build();

                        // Pass the options and the username into the custom FirestoreRecyclerAdapter class
                        this.myFirestoreRecyclerAdapter = new messageAdapter(options, username);
                        this.myFirestoreRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                recyclerView.scrollToPosition(myFirestoreRecyclerAdapter.getItemCount() - 1);
                            }
                        });

                        // Assign the adapter to the RecyclerView
                        this.recyclerView.setAdapter(this.myFirestoreRecyclerAdapter);

                        // Set the layout for the RecyclerView
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                        linearLayoutManager.setStackFromEnd(true);
                        linearLayoutManager.setSmoothScrollbarEnabled(true);
                        this.recyclerView.setLayoutManager(linearLayoutManager);

                        // Start listening for changes in the data
                        this.myFirestoreRecyclerAdapter.startListening();
                    }
                });

        // Send button logic
        this.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEtv.getText().toString();

                // Ready the values of the message
                Map<String, Object> data = new HashMap<>();
                data.put("username", username);
                data.put("message", message);
                data.put("timestamp", FieldValue.serverTimestamp());

                // Send the data off to the Message collection within the specific chatroom
                dbRef.collection("chatrooms")
                        .document(chatroomId)
                        .collection("messages")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // "Reset" the message in the EditText
                                messageEtv.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                // Handle the error
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.myFirestoreRecyclerAdapter != null) {
            this.myFirestoreRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.myFirestoreRecyclerAdapter != null) {
            this.myFirestoreRecyclerAdapter.stopListening();
        }
    }
}