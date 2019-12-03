package com.example.securep2pcomm.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.adapters.SecureRoomAdapter;
import com.example.securep2pcomm.helpers.SecureRoomChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.example.securep2pcomm.ui.MainFragment.TAG;

public class RoomsFragment extends Fragment {

    private RecyclerView mrecycler;
    private SecureRoomAdapter secureRoomAdapter;


    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;

    public RoomsFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_rooms, container, false);
        mrecycler = rootview.findViewById(R.id.roomList);

        mrecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        loadRooms();

        return rootview;
    }

    private void getRooms(EventListener<QuerySnapshot> listener1) {
        db.collection("room")
                .whereEqualTo("full", true)
                .orderBy("name")
                .addSnapshotListener(listener1);
    }

    private void loadRooms(){
        getRooms(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("RoomsFragment", "Listen failed.", e);
                    return;
                }

                ArrayList<SecureRoomChat> rm = new ArrayList<>();
                for(QueryDocumentSnapshot doc: snapshots) {
                    String owner = doc.getString("owner");
                    String guest = doc.getString("guest");
                    if (owner.equals(currentFirebaseUser.getUid()) || guest.equals(currentFirebaseUser.getUid())) {
                        rm.add(
                                new SecureRoomChat(
                                        doc.getId(),
                                        doc.getString("name"),
                                        doc.getString("owner"),
                                        doc.getString("owner_name"),
                                        doc.getString("guest"),
                                        doc.getBoolean("full")
                                )
                        );

                    }
                }

                secureRoomAdapter = new SecureRoomAdapter(rm, listener);
                mrecycler.setAdapter(secureRoomAdapter);
            }
        });

    }

    SecureRoomAdapter.OnSecureRoomClickListener listener = new SecureRoomAdapter.OnSecureRoomClickListener() {
        @Override
        public void onClick(SecureRoomChat clicked) {
            Log.i(TAG, "onClick: " + clicked.getID());
            RoomFragment mess = RoomFragment.newInstance(clicked.getID(), clicked.getOwner(), clicked.getOwner_name(), clicked.getGuest(), currentFirebaseUser.getUid());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mess)
                    .commit();

        }
    };


}
