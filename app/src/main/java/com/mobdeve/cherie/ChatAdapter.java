package com.mobdeve.cherie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<userData> matches;
    private Context context;
    private FirebaseFirestore dbRef;
    private FirebaseAuth mAuth;

    private String currentUsername;

    public ChatAdapter(Context context, List<userData> matches) {
        this.context = context;
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userData match = matches.get(position);
        holder.name.setText(match.getName());
        // Load images?
        dbRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        holder.itemView.setOnClickListener(v -> {
            // check chatroom if it exists (it should kase nagmatch na sila)
            dbRef.collection("chatrooms")
                    .whereEqualTo("userId1", mAuth.getUid())
                    .whereEqualTo("userId2", match.getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        //check in userid1 and userid2
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String chatroomId = task.getResult().getDocuments().get(0).getId();
                            Intent i = new Intent(context, ChatroomActivity.class);
                            i.putExtra("chatroomId", chatroomId);
                            context.startActivity(i);
                        } else {
                            //check in userid2 and userid1
                            dbRef.collection("chatrooms")
                                    .whereEqualTo("userId1", match.getUserId())
                                    .whereEqualTo("userId2", mAuth.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                                            String chatroomId = task1.getResult().getDocuments().get(0).getId();
                                            Intent i = new Intent(context, ChatroomActivity.class);
                                            i.putExtra("chatroomId", chatroomId);
                                            context.startActivity(i);
                                        }
                                    });
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_name);
            profileImage = itemView.findViewById(R.id.image_view_profile);
        }
    }
}