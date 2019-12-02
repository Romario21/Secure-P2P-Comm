package com.example.securep2pcomm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.helpers.Room;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>{

    public interface OnOptionsClickListener{
        void onClick(Room clicked);
    }

    private OnOptionsClickListener mListener;
    private ArrayList<Room> rooms;
    private Context mContext;

    public OptionsAdapter(ArrayList<Room> rms, OnOptionsClickListener clicked){
        rooms = rms;
        this.mListener = clicked;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rooms, parent, false);
        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class OptionsViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Room rm;

        public OptionsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.room_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(rm);
                }
            });
        }

        public void bind(Room chat2){
            this.rm = chat2;
            name.setText(rm.getName());
        }
    }
}
