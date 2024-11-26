package com.mobdeve.cherie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
    private int CHAT_BAR_MAX = 75;
    private TextView otherUserTv;
    private EditText messageEtv;
    private Button sendBtn;
    private ProgressBar progressBar;

    // RecyclerView Components
    private RecyclerView recyclerView;
    // Replacement of the base adapter view
    private messageAdapter myFirestoreRecyclerAdapter;

    // DB reference
    private FirebaseFirestore dbRef;
    private String chatroomId;
    private String username;
    private String otherUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        this.otherUserTv = findViewById(R.id.otherUserTextView);
        this.messageEtv = findViewById(R.id.messageEtv);
        this.sendBtn = findViewById(R.id.sendBtn);
        this.recyclerView = findViewById(R.id.chatRecyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setMax(CHAT_BAR_MAX);

        // Get intents
        this.chatroomId = getIntent().getStringExtra("chatroomId");
        this.otherUsername = getIntent().getStringExtra("otherUsername");
        this.otherUserTv.setText(otherUsername);
        // Initialize Firebase things
        dbRef = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef.collection("users").document(currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userData currentUserData = task.getResult().toObject(userData.class);
                        this.username = currentUserData.getName();


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
                                updateProgressBar();
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
                                updateProgressBar();
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

    // This function makes it a realtime chatroom by constantly listening
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
    private void updateProgressBar() {
        dbRef.collection("chatrooms")
                .document(chatroomId)
                .collection("messages")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int messageCount = task.getResult().size();
                        int progress = messageCount;
                        progressBar.setProgress(progress);
                    }
                });
    }
}