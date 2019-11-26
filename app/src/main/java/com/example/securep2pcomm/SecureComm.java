package com.example.securep2pcomm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecureComm extends AppCompatActivity {

    public static final String TAG = SecureComm.class.getCanonicalName();

    private BottomNavigationView nav;
    private BottomNavigationView.OnNavigationItemSelectedListener mNavSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_rooms:
                    return true;

                case R.id.navigation_home:
                    MainFragment main = new MainFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, main)
                            .commit();
                    return true;

                case R.id.navigation_options:
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_comm);

        nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mNavSelectedListener);

        //MainFragment
        MainFragment frag = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, frag)
                . addToBackStack(MainFragment.TAG)
                .commit();

        nav.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
    }
}
