package com.mobdeve.cherie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.cherie.UserData;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<UserData> profiles;

    public ProfileAdapter(List<UserData> profiles) {
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
        UserData profile = profiles.get(position);

        String nameAge = profile.getName() + ", " + profile.getAge();

        holder.nameAge.setText(nameAge);
        holder.bio.setText(profile.getBio());
        holder.hobby.setText(profile.getHobby());
        holder.preferencesLocation.setText("dating in " + profile.getLocation());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameAge;
        TextView bio;
        TextView hobby;
        TextView preferencesLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAge = itemView.findViewById(R.id.name_age);
            bio = itemView.findViewById(R.id.bio);
            hobby = itemView.findViewById(R.id.hobbies);
            preferencesLocation = itemView.findViewById(R.id.preferences_location);
        }
    }
}