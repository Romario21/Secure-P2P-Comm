package com.example.securep2pcomm.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.example.securep2pcomm.helpers.Messages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;
import static com.example.securep2pcomm.ui.OptionsFragment.TAG;

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
    private static final String ARG_PARAM2 = "owner_id";
    private static final String ARG_PARAM3 = "owner_name";
    private static final String ARG_PARAM4 = "guest_id";

    private String room_id;
    private String owner_id;
    private String owner_name;
    private String guest_id;

    private String displayname;
    private String ownName;


    //might need to change
    public static RoomFragment newInstance(String RoomId, String owner, String ownerName, String guest){
        RoomFragment frag = new RoomFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, RoomId);
        args.putString(ARG_PARAM2, owner);
        args.putString(ARG_PARAM3, ownerName);
        args.putString(ARG_PARAM4, guest);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            room_id = getArguments().getString(ARG_PARAM1);
            owner_id = getArguments().getString(ARG_PARAM2);
            owner_name = getArguments().getString(ARG_PARAM3);
            guest_id = getArguments().getString(ARG_PARAM4);
        }
    }

    public RoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_room, container, false);

        message = rootview.findViewById(R.id.message);
        mButton = rootview.findViewById(R.id.send);
        leave = rootview.findViewById(R.id.leave);
        mDisplay = rootview.findViewById(R.id.name);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(currentFirebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ownName = documentSnapshot.getString("name");
                    }
                });

        if(!owner_id.equals(currentFirebaseUser.getUid()))
            displayname = owner_name;
        else{
            db.collection("users")
                    .document(guest_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            displayname = documentSnapshot.getString("name");
                        }
                    });
        }
        mDisplay.setText(displayname);

        if(owner_id.equals(currentFirebaseUser.getUid()))
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

                db.collection("room")
                        .document(room_id)
                        .update("guest", "0");

                db.collection("room")
                        .document(room_id)
                        .update("full", false);

                db.collection("chat")
                        .whereEqualTo("room", room_id)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshot) {
                                for (QueryDocumentSnapshot doc : documentSnapshot) {
                                    removemessage(doc.getId());
                                }
                            }
                        });

                exitFrag();
            }
        });


        chats = rootview.findViewById(R.id.chat);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        chats.setLayoutManager(manager);

        if(getArguments() != null){
            displayChat();
        }

        return rootview;
    }

    private void removemessage(String id) {
        db.collection("chat")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void exitFrag() {
        MainFragment frag = new MainFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .add(R.id.fragment_container, frag)
                .commit();
    }

    private void getChat(EventListener<QuerySnapshot> listener) {
        db.collection("chat")
                .orderBy("sent", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }

    private void displayChat() {
        getChat(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("PrivateMessageFragment", "Listen failed.", e);
                    return;
                }
                ArrayList<Messages> messages = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    String roomID = doc.getString("room");

                    if (roomID.equals(room_id)){
                        messages.add(
                                new Messages(
                                        doc.getString("room"),
                                        doc.getString("sender"),
                                        doc.getString("receiver"),
                                        doc.getString("sender_name"),
                                        doc.getString("message"),
                                        doc.getLong("sent")
                                )
                        );
                    }
                }

                mAdapter = new MessageAdapter(messages, currentFirebaseUser.getUid());
                chats.setAdapter(mAdapter);

            }
        });
    }

    private void sendMessage() {
        String send = message.getText().toString();
        String sender = currentFirebaseUser.getUid();
        String receiver;

        if (guest_id.equals(sender))
            receiver = owner_id;
        else
            receiver = guest_id;

        message.setText("");
        mButton.setEnabled(false);

        Map<String, Object> chat = new HashMap<>();
        chat.put("room", room_id);
        chat.put("sender", sender);
        chat.put("receiver", receiver);
        chat.put("sender_name", ownName);
        chat.put("message", send);
        chat.put("sent", System.currentTimeMillis());

        db.collection("chat")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mButton.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"message failed to send",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
