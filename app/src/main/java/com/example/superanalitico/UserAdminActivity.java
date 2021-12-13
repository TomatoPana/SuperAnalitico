package com.example.superanalitico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.superanalitico.orm.Users;
import com.example.superanalitico.utils.DataAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserAdminActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView usersRecyclerView;
    private DataAdapter adapter;
    FirebaseAuth mAuth;
    SharedPreferences savedUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

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

        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        initializeRecyclerView();
    }

    protected void initializeRecyclerView() {

        List<Map<String, Object>> usersDataSet = new ArrayList<>();

        usersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        usersRecyclerView.setLayoutManager(layoutManager);

        adapter = new DataAdapter(usersDataSet, getBaseContext());
        usersRecyclerView.setAdapter(adapter);

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        CollectionReference database = mFirebase.collection("users");
        database.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot:
                        Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> user = documentSnapshot.getData();
                    usersDataSet.add(user);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}