package com.mobdeve.cherie;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.List;

public class MatchScrollAdapter extends RecyclerView.Adapter<MatchScrollAdapter.ViewHolder> {

    private List<UserData> matches;
    private Context context;

    public MatchScrollAdapter(Context context, List<UserData> matches) {
        this.matches = matches;
        this.context = context;
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

        Glide.with(this.context)
                .load(match.getImageUrl())
                .transform(new CircleCrop())
                .into(holder.profileImage);

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
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAgeView = itemView.findViewById(R.id.match_scroll_name_age);
            bioView = itemView.findViewById(R.id.match_scroll_bio);
            profileImage = itemView.findViewById(R.id.image_view_profile);
        }
    }
}