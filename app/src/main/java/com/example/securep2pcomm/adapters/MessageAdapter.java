package com.example.securep2pcomm.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.helpers.Messages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private String userId;
    private ArrayList<Messages> mess;

    public interface OnMessageClickListener{
        void onClick(Messages clicked);
    }

    private OnMessageClickListener clicked;

    public MessageAdapter(){

    }

    public MessageAdapter(ArrayList<Messages> mess, OnMessageClickListener clicked,String userId){
        this.mess = mess;
        this.userId = userId;
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sent_chat, parent, false);
        }

        else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_received_chat, parent, false);
        }

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(mess.get(position));
    }

    @Override
    public int getItemViewType(int position){
        if(mess.get(position).getSenderId().contentEquals(userId))
            return 0;

        return 1;
    }

    @Override
    public int getItemCount() {
        return mess.size();
    }










    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView timestamp;
        Messages received;
        ImageView profile;


        public MessageViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.chat_message);
            timestamp = itemView.findViewById(R.id.timestamp);
            profile = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onClick(received);
                }
            });
        }

        public void bind(final Messages chat){

            received = chat;
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            timestamp.setText(chat.getSenderName());
            message.setText(chat.getMessage());
        }

    }
}
