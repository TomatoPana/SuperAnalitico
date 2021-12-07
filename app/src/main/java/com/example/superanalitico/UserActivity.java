package com.example.superanalitico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getAllDocuments();
    }

    protected void getAllDocuments() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");
        data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot allData = task.getResult();
                    assert allData != null;
                    List<Exchange> info = allData.toObjects(Exchange.class);
                    Log.d("FirebaseUtil", allData.getDocuments().get(0).toString());
                }
            }
        });
    }
}