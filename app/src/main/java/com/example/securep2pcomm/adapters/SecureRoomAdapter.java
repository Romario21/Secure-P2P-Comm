package com.example.securep2pcomm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.helpers.SecureRoomChat;

import java.util.ArrayList;

public class SecureRoomAdapter extends RecyclerView.Adapter<SecureRoomAdapter.SecureViewHolder>{

    public interface OnSecureRoomClickListener{
        void onClick(SecureRoomChat clicked);
    }

    private OnSecureRoomClickListener mListener;
    private ArrayList<SecureRoomChat> rooms;

    public SecureRoomAdapter(ArrayList<SecureRoomChat> rm, OnSecureRoomClickListener clicked){
        rooms = rm;
        this.mListener = clicked;
    }

    @NonNull
    @Override
    public SecureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rooms, parent, false);
        return new SecureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SecureViewHolder holder, int position) {
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class SecureViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        SecureRoomChat chat;

        public SecureViewHolder(View itemview){
            super(itemview);
            name = itemview.findViewById(R.id.room_name);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(chat);
                }
            });
        }

        public void bind(SecureRoomChat chat2){
            this.chat = chat2;
            name.setText(chat2.getRoom_name());
        }
    }

}
