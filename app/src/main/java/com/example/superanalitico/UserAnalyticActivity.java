package com.example.superanalitico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class UserAnalyticActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    FirebaseAuth mAuth;
    SharedPreferences savedUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_analitic);

        mAuth = FirebaseAuth.getInstance();
        savedUserData = getSharedPreferences("savedUserData", MODE_PRIVATE);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(item -> {
            if ("Cerrar Sesion".equals(item.getTitle().toString())) {
                mAuth.signOut();
                SharedPreferences.Editor writer = savedUserData.edit();
                writer.clear();
                writer.apply();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                startActivity(logoutIntent);
                return true;
            }
            return false;
        });

    }
}