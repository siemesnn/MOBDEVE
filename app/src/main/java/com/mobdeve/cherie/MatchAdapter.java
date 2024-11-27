package com.mobdeve.cherie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private List<UserData> matches;
    private Context context;
    private FirebaseFirestore dbRef;
    private FirebaseAuth mAuth;

    public MatchAdapter(Context context, List<UserData> matches) {
        this.matches = matches;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData match = matches.get(position);
        holder.name.setText(match.getName());
        // Load images?
        Glide.with(this.context)
                .load(match.getImageUrl())
                .transform(new CircleCrop())
                .into(holder.profileImage);

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
                            i.putExtra("otherUsername", match.getName());
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
                                            i.putExtra("otherUsername", match.getName());
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
        ImageView profileImage;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_view_match);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}