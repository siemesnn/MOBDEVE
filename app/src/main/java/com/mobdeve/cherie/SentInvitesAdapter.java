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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SentInvitesAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private List<UserData> matches;
    private Context context;
    private FirebaseFirestore dbRef;
    private FirebaseAuth mAuth;

    public SentInvitesAdapter(List<UserData> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite, parent, false);
        return new MatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder holder, int position) {
        UserData match = matches.get(position);
        holder.name.setText(match.getName());
        // Load images?
        dbRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        holder.itemView.setOnClickListener(v -> {
           //do stuff like check profile? with less info
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}
