package com.mobdeve.cherie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<userData> profiles;

    public ProfileAdapter(List<userData> profiles) {
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userData profile = profiles.get(position);

        String nameAge = profile.getName() + ", " + profile.getAge();

        holder.nameAge.setText(nameAge);
        holder.bio.setText(profile.getBio());
        holder.hobby.setText(profile.getHobby());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameAge;
        TextView bio;
        TextView hobby;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAge = itemView.findViewById(R.id.name_age);
            bio = itemView.findViewById(R.id.bio);
            hobby = itemView.findViewById(R.id.hobbies);
        }
    }
}