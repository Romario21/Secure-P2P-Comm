package com.example.securep2pcomm.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;


import com.example.securep2pcomm.R;
import com.example.securep2pcomm.adapters.RoomsAdapter;
import com.example.securep2pcomm.helpers.AvailableRoom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getCanonicalName();
    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;

    private RecyclerView recyclerView;
    private RoomsAdapter roomAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = rootview.findViewById(R.id.available);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        db = FirebaseFirestore.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        loadOpenRooms();

        return rootview;
    }

    private void getOpenRooms(EventListener<QuerySnapshot> listener){
        db.collection("room")
                .whereEqualTo("guest", "0")
                .orderBy("name")
                .addSnapshotListener(listener);
    }

    private void loadOpenRooms(){
        getOpenRooms(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null){
                    Log.e("MainFragment", "Listener Failed", e);
                }

                ArrayList<AvailableRoom> rm = new ArrayList<>();

                for (QueryDocumentSnapshot doc : snapshots) {
                    String owner = doc.getString("owner");
                    if (owner.equals(currentFirebaseUser.getUid()))
                        continue;

                    rm.add(
                            new AvailableRoom(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("owner"),
                                    doc.getString("owner_name"),
                                    doc.getString("guest"),
                                    doc.getBoolean("full")
                            )
                    );
                }

                roomAdapter = new RoomsAdapter(rm, listener);
                recyclerView.setAdapter(roomAdapter);
            }
        });
    }

    RoomsAdapter.OnRoomClickListener listener = new RoomsAdapter.OnRoomClickListener() {
        @Override
        public void onClick(final AvailableRoom clicked) {
            Log.i(TAG, "onClick: " + clicked.getID());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Room");
            builder.setMessage("Start Secure Communication with " + clicked.getOwner_name());
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Add the user to the available room and mark as full
                    //will no longer appear as available
                    db.collection("room")
                            .document(clicked.getID())
                            .update("guest", currentFirebaseUser.getUid());

                    db.collection("room")
                            .document(clicked.getID())
                            .update("full", true);

                    /*RoomFragment mess = RoomFragment.newInstance(clicked.getID(), clicked.getOwner(), clicked.getOwner_name(), clicked.getGuest(), currentFirebaseUser.getUid());
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, mess)
                            .commit();*/
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        }
    };


}
