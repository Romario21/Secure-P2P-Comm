package com.example.securep2pcomm.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securep2pcomm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getCanonicalName();
    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;

    //private ClassAdapter mAdapter;
    private RecyclerView recyclerView;


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

        /*db.collection("temp")
                .document(currentFirebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //pupt a romm function
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "An error occure with firestore")
            }
        });*/

        return rootview;
    }


    /*
    private void getClasses(EventListener<QuerySnapshot> listener) {
        db.collection("class")
                .whereEqualTo("student", currentFirebaseUser.getUid())
                .orderBy("name")
                .addSnapshotListener(listener);
    }

    private void loadClasses() {

    }*/


}
