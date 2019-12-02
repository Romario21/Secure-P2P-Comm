package com.example.securep2pcomm.ui;

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
import com.example.securep2pcomm.helpers.RoomChat;
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

import javax.annotation.Nullable;


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

        db.collection("users")
                .document(currentFirebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String test = documentSnapshot.getString("active");
                        //if(test.equals("true")){
                            //loadOpenRooms();
                        //}
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                // Log.d("Tag",e.toString());
                Log.i(TAG, "onFailure:");
            }
        });

        return rootview;
    }
    /*
    private void getAllRooms(EventListener<QuerySnapshot> listener){
        db.collection("room")
                .whereEqualTo("student", currentFirebaseUser.getUid())
                .orderBy("name")
                .addSnapshotListener(listener);
    }

    private void loadOpenRooms(){
        getAllRooms(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.e("MainFragment", "Listener Failed", e);
                }

                ArrayList<RoomChat> rm = new ArrayList<>();

                for (QueryDocumentSnapshot doc : snapshots) {
                    rm.add(
                            new RoomChat(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("room")
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
        public void onClick(RoomChat clicked) {
            Log.i(TAG, "onClick: " + clicked.getID());

        }
    };*/


}
