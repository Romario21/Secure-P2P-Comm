package com.example.securep2pcomm.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.securep2pcomm.R;
import com.example.securep2pcomm.SecureComm;
import com.example.securep2pcomm.StartScreen;
import com.example.securep2pcomm.adapters.OptionsAdapter;
import com.example.securep2pcomm.adapters.RoomsAdapter;
import com.example.securep2pcomm.helpers.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptionsFragment extends Fragment {
    public static final String TAG = OptionsFragment.class.getCanonicalName();

    private FloatingActionButton add;
    private String getEntry;
    private OptionsAdapter mAdapter;
    private Button logout;


    private FirebaseUser currentFirebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private String name;

    public OptionsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_options, container, false);

        add = v.findViewById(R.id.addButton);
        logout = v.findViewById(R.id.logout);
        recyclerView = v.findViewById(R.id.openRoomsList);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        db.collection("users")
                .document(currentFirebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name = documentSnapshot.getString("name");
                    }
                });

        loadMyRooms();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create Room");
                builder.setMessage("Enter a name for the room.");
                final EditText classEntry = new EditText(getActivity());
                builder.setView(classEntry);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getEntry = classEntry.getText().toString();

                        Map<String, Object> classAdd = new HashMap<>();
                        classAdd.put("name", getEntry);
                        classAdd.put("owner", currentFirebaseUser.getUid());
                        classAdd.put("owner_name", name);
                        classAdd.put("guest", "0");
                        classAdd.put("full", false);

                        db.collection("room")
                                .add(classAdd)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.i(TAG, "onSuccess: ");

                                        Map<String, Object> addRoom = new HashMap<>();
                                        addRoom.put("name",getEntry);
                                        db.collection("users")
                                                .document(currentFirebaseUser.getUid())
                                                .collection("rooms")
                                                .document(documentReference.getId())
                                                .set(addRoom);

                                        loadMyRooms();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),"failed to add class",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), StartScreen.class));
            }
        });

        return v;
    }

    private void getMyRooms(EventListener<QuerySnapshot> listener2){
        db.collection("room")
                .whereEqualTo("owner", currentFirebaseUser.getUid())
                .orderBy("name")
                .addSnapshotListener(listener2);
    }

    private void loadMyRooms() {
        getMyRooms( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ClassesFragment", "Listen failed.", e);
                    return;
                }
                ArrayList<Room> rms = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    rms.add(
                            new Room(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getBoolean("full")
                            )
                    );
                }

                mAdapter = new OptionsAdapter(rms, listener);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }

    OptionsAdapter.OnOptionsClickListener listener = new OptionsAdapter.OnOptionsClickListener() {
        @Override
        public void onClick(final Room clicked) {
            Log.i(TAG, "onClick: " + clicked.getID());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete Room");
            builder.setMessage("The room will be deleted along with any messages within.");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.collection("room")
                            .document(clicked.getID())
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

                    db.collection("users")
                            .document(currentFirebaseUser.getUid())
                            .collection("rooms")
                            .document(clicked.getID())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(getActivity(), "Room '" + clicked.getName() + "' was deleted",
                                            Toast.LENGTH_SHORT).show();
                                    loadMyRooms();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    };
}
