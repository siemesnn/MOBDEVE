package com.mobdeve.cherie;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
/*
 * The FirestoreRecyclerAdapter is is a modification of the regular Adapter and is able to integrate
 * with Firestore. According to the documentation: [The Adapter] binds a Query to a RecyclerView and
 * responds to all real-time events included items being added, removed, moved, or changed. Best
 * used with small result sets since all results are loaded at once.
 * See https://firebaseopensource.com/projects/firebase/firebaseui-android/firestore/readme/
 * */

public class messageAdapter extends FirestoreRecyclerAdapter<messageData, MessageViewHolder> {

    // We need to know who the current user is so that we can adjust their message to the right of
    // the screen and those who aren't to the left of the screen
    private String username;

    public messageAdapter(FirestoreRecyclerOptions<messageData> options, String username) {
        super(options);
        this.username = username;
    }

    // Good old onCreateViewHolder. Nothing different here.
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        MessageViewHolder messageViewHolder = new MessageViewHolder(v);
        return messageViewHolder;
    }

    // The onBindViewHolder is slightly different as you also get the "model". It was clear from the
    // documentation, but it seems that its discouraging the use of the position parameter. The
    // model passed in is actually the respective model that is about to be bound. Hence, why we
    // don't use position, and directly get the information from the model parameter.
    @Override
    protected void onBindViewHolder(MessageViewHolder holder, int position, messageData model) {
        holder.bindData(model);

        // Change alignment depending on whether the message is of the current user or not
        if(username != null && username.equals(model.getUsername())) { // Right align
            holder.rightAlignText();
        } else { // Left align
            holder.leftAlignText();
        }
    }
}