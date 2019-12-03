package com.example.securep2pcomm.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.adapters.MessageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class RoomFragment extends Fragment {

    private MessageAdapter mAdapter;

    private EditText message;
    private ImageButton mButton;
    private TextView mDisplay;
    private FirebaseUser currentFirebaseUser;
    private FirebaseFirestore db;
    private RecyclerView chats;

    private Button leave;

    private static final String ARG_PARAM1 = "room_id";
    private static final String ARG_PARAM2 = "room_name";

    private String user_id;
    private String user_name;

    //might need to change
    public static RoomFragment newInstance(String Id, String Name){
        RoomFragment frag = new RoomFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Id);
        args.putString(ARG_PARAM2, Name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(ARG_PARAM1);
            user_name = getArguments().getString(ARG_PARAM2);
        }
    }

    public RoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_room, container, false);

        message = rootview.findViewById(R.id.message);
        mButton = rootview.findViewById(R.id.send);
        leave = rootview.findViewById(R.id.leave);
        mDisplay = rootview.findViewById(R.id.name);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        db = FirebaseFirestore.getInstance();

        mDisplay.setText(user_name);

        if(user_id.equals(currentFirebaseUser.getUid()))
            leave.setVisibility(View.INVISIBLE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message.getText().toString().equals("")&& getArguments() != null){
                    sendMessage();
                }
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                db.collection("room")
                        .document(clicked.getID())
                        .update("guest", currentFirebaseUser.getUid());

                db.collection("room")
                        .document(clicked.getID())
                        .update("full", true);*/

                exitFrag();
            }
        });


        chats = rootview.findViewById(R.id.chat);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        chats.setLayoutManager(manager);

        return rootview;
    }

    private void exitFrag() {
        MainFragment frag = new MainFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .add(R.id.fragment_container, frag)
                .commit();
    }

    private void sendMessage() {
        String send = message.getText().toString();
        String sender = currentFirebaseUser.getUid();

    }


}
