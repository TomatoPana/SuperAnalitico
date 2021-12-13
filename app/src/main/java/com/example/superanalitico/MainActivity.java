package com.example.superanalitico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences savedUserData;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;
    FirebaseAuth mAuth;
    boolean isLogged;
    FirebaseFirestore mDatabase;

    private void redirectLoggedUser(String Uid) {
        final String TAG = "Firebase";
        mDatabase = FirebaseFirestore.getInstance();
        CollectionReference userCollection = mDatabase.collection("users");
        DocumentReference userDocument = userCollection.document(Uid);
        userDocument.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Intent intent;
                    switch (String.valueOf(Objects.requireNonNull(document.getData()).get("user_type"))) {
                        case "user":
                        default:
                            intent = new Intent(MainActivity.this, UserActivity.class);
                            break;
                        case "analytic":
                            intent = new Intent(MainActivity.this, UserAnalyticActivity.class);
                            break;
                        case "admin":
                            intent = new Intent(MainActivity.this, UserAdminActivity.class);
                            break;
                    }
                    startActivity(intent);
                } else {
                    Log.d(TAG, "No such document");
                    // Create document with defaults values
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public void firebaseLogin(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        SharedPreferences.Editor writer = savedUserData.edit();
                        writer.putString("email", email);
                        writer.putString("password", password);
                        writer.apply();
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        redirectLoggedUser(mAuth.getCurrentUser().getUid());
                    } else {
                        Toast.makeText(this, "Incorrect User or Password", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean verifyLoginStatus() {
        mAuth = FirebaseAuth.getInstance();
        savedUserData = getSharedPreferences("savedUserData", MODE_PRIVATE);
        String savedEmail = savedUserData.getString("email", "");
        String savedPassword = "";
        if(savedEmail.length() == 0){
            return false;
        } else {
            savedPassword = savedUserData.getString("password", "");
            firebaseLogin(savedEmail, savedPassword);
        }
        return (mAuth.getCurrentUser() != null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(!verifyLoginStatus()){
            setContentView(R.layout.activity_main);
            loginButton = findViewById(R.id.button);
            registerButton = findViewById(R.id.button2);
            emailText = findViewById(R.id.editTextTextEmailAddress);
            passwordText = findViewById(R.id.editTextTextPassword);
            isLogged = false; // Only for pre initializing

            loginButton.setOnClickListener(view -> {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if (email.length() == 0) {
                    emailText.setError("The Email is Required");
                    return;
                }
                if (password.length() == 0) {
                    passwordText.setError("The Password is Required");
                    return;
                }
                firebaseLogin(email, password);
                if(mAuth.getCurrentUser() != null) {
                    Toast.makeText(this, "Incorrect User or Password", Toast.LENGTH_LONG).show();
                }
            });
            registerButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    }
}