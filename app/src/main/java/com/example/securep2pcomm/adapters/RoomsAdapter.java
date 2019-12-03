package com.example.securep2pcomm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.helpers.AvailableRoom;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>{

    public interface OnRoomClickListener{
        void onClick(AvailableRoom clicked);
    }

    private OnRoomClickListener mListener;
    private ArrayList<AvailableRoom> rooms;

    public RoomsAdapter(ArrayList<AvailableRoom> rm, OnRoomClickListener clicked){
        rooms = rm;
        this.mListener = clicked;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rooms, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        AvailableRoom chat;

        public RoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.room_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(chat);
                }
            });
        }

        public void bind(AvailableRoom chat){
            this.chat = chat;
            name.setText(chat.getRoom_name());
        }

    }

}
