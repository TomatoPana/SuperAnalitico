package com.example.superanalitico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.superanalitico.orm.Users;
import com.example.superanalitico.utils.DataAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class UserAdminActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView usersRecyclerView;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(item -> {
            if ("Logout".equals(item.getTitle().toString())) {
                Toast.makeText(UserAdminActivity.this, "Logging out", Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });

        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        initializeRecyclerView();
    }

    protected List<Users> getAllUserInfoFromFirebase() {
        List<Users> usersDataSet = new ArrayList<>();



        return usersDataSet;
    }

    protected void initializeRecyclerView() {

        List<Users> usersDataSet = new ArrayList<>();
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));
        usersDataSet.add(new Users("Lozano Bobadilla", "Moises David", Users.USER, "mdlb.lobo@gmail.com"));

        usersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        usersRecyclerView.setLayoutManager(layoutManager);

        adapter = new DataAdapter(usersDataSet, getBaseContext());
        usersRecyclerView.setAdapter(adapter);

    }
}