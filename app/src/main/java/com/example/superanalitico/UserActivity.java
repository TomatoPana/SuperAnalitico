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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // getAllDocuments();
        // getDocument("1");
        // createDocument();
        // Exchange info = new Exchange();
        // info.amount = 20000;
        // info.created_at = new Date();
        // info.description = "Another";
        // info.exchange_type = "daily";
        // info.subcategory = new ArrayList<>();
        // info.subcategory.add("Element");
        // info.updated_at = new Date();
        // editDocument("1", info);
        // deleteDocument("1");
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
                    Log.d("FirebaseUtil", String.valueOf(info.get(0).amount));
                    Log.d("FirebaseUtil", info.get(0).created_at.toString());
                    Log.d("FirebaseUtil", String.valueOf(info.get(0).description));
                    Log.d("FirebaseUtil", String.valueOf(info.get(0).exchange_type));
                    Log.d("FirebaseUtil", info.get(0).subcategory.toString());
                    Log.d("FirebaseUtil", info.get(0).updated_at.toString());
                }
            }
        });
    }

    protected void createDocument() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");

        Exchange info = new Exchange();

        info.amount = 20000;
        info.created_at = new Date();
        info.description = "Another";
        info.exchange_type = "daily";
        info.subcategory = new ArrayList<>();
        info.subcategory.add("Element");
        info.updated_at = new Date();

        data.add(info).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    Log.d("FirebaseUtil", "Data Created");
                }
            }
        });
    }

    protected void getDocument(String id) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");
        data.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot data = task.getResult();
                    assert data != null;
                    Exchange info = data.toObject(Exchange.class);
                    Log.d("FirebaseUtil", String.valueOf(info.amount));
                    Log.d("FirebaseUtil", info.created_at.toString());
                    Log.d("FirebaseUtil", String.valueOf(info.description));
                    Log.d("FirebaseUtil", String.valueOf(info.exchange_type));
                    Log.d("FirebaseUtil", info.subcategory.toString());
                    Log.d("FirebaseUtil", info.updated_at.toString());
                }

            }
        });
    }

    protected void editDocument(String id, Exchange newData) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");
        data.document(id).set(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("FirebaseUtil", "Data changed");
                }
            }
        });
    }

    protected void deleteDocument(String id) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
        DocumentReference database = mFirebase.collection("users").document(uid);

        CollectionReference data = database.collection("exchanges");
        data.document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("FirebaseUtil", "Data deleted");
                }
            }
        });
    }
}