package com.example.superanalitico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextAge;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    SharedPreferences savedUserData;
    FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
    }

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
                            intent = new Intent(RegisterActivity.this, UserActivity.class);
                            break;
                        case "analytic":
                            intent = new Intent(RegisterActivity.this, UserAnalyticActivity.class);
                            break;
                        case "admin":
                            intent = new Intent(RegisterActivity.this, UserAdminActivity.class);
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

    public void onRegisterClickListener(View view) {
        if(editTextFirstName.getText().length() == 0) {
            editTextFirstName.setError("This field cannot be empty");
            return;
        }
        if(editTextLastName.getText().length() == 0) {
            editTextLastName.setError("This field cannot be empty");
            return;
        }
        if(editTextAge.getText().length() == 0) {
            editTextAge.setError("This field cannot be empty");
            return;
        }
        if(editTextEmail.getText().length() == 0) {
            editTextEmail.setError("This field cannot be empty");
            return;
        }
        if(editTextPassword.getText().length() == 0) {
            editTextPassword.setError("This field cannot be empty");
            return;
        }
        if(editTextConfirmPassword.getText().length() == 0) {
            editTextConfirmPassword.setError("This field cannot be empty");
            return;
        }
        if(!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            editTextPassword.setError("Passwords do not match");
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextConfirmPassword.getText().toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d("Register", Objects.requireNonNull(task.getResult()).toString());
                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextConfirmPassword.getText().toString()).addOnCompleteListener(loginTask -> {
                    if(task.isSuccessful()){
                        SharedPreferences.Editor writer = savedUserData.edit();
                        writer.putString("email", editTextEmail.getText().toString());
                        writer.putString("password", editTextConfirmPassword.getText().toString());
                        writer.apply();
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        redirectLoggedUser(mAuth.getCurrentUser().getUid());
                    }
                });
            } else {
                Log.d("Register", Objects.requireNonNull(task.getException()).toString());
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                builder.setTitle("User already register")
                        .setMessage("Please choose another email")
                        .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    public void onBackClickListener(View view) {
        finish();
    }
}