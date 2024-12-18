package com.mobdeve.cherie;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView usernameTv, messageTv;

    public MessageViewHolder(View itemView) {
        super(itemView);
        this.usernameTv = itemView.findViewById(R.id.usernameTv);
        this.messageTv = itemView.findViewById(R.id.messageTv);
    }

    public void bindData(messageData m) {
        this.usernameTv.setText(m.getUsername());
        this.messageTv.setText(m.getMessage());
    }

    public void leftAlignText() {
        this.usernameTv.setGravity(Gravity.LEFT);
        this.messageTv.setGravity(Gravity.LEFT);
    }

    public void rightAlignText() {
        this.usernameTv.setGravity(Gravity.RIGHT);
        this.messageTv.setGravity(Gravity.RIGHT);
    }
}
