package com.mobdeve.cherie;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchScrollAdapter extends RecyclerView.Adapter<MatchScrollAdapter.ViewHolder> {

    private List<UserData> matches;

    public MatchScrollAdapter(List<UserData> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scroll_matches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData match = matches.get(position);
        String nameAge = match.getName() + ", " + match.getAge();
        holder.nameAgeView.setText(nameAge);
        holder.bioView.setText(match.getBio());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), InspectProfileActivity.class);
                i.putExtra("id", match.getUserId());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameAgeView;
        TextView bioView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAgeView = itemView.findViewById(R.id.match_scroll_name_age);
            bioView = itemView.findViewById(R.id.match_scroll_bio);
        }
    }
}