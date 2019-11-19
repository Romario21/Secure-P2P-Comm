package com.example.securep2pcomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;


public class LoadScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() == null){
                    startActivity(new Intent(LoadScreen.this, StartScreen.class));
                    finish();
                }

                else {
                    startActivity(new Intent(LoadScreen.this, StartScreen.class));
                    finish();
                }
            }
        }, 3000);

    }
}
