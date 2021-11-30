package com.example.superanalitico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    SharedPreferences savedUserData;
    boolean isLogged;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    FirebaseAuth mAuth;

    public void setLoginStatus(String email, String password) {
        if(isLogged) {
            emailText.setText(email);
            emailText.setEnabled(false);
            passwordText.setText(password);
            passwordText.setEnabled(false);
            loginButton.setText("Logout");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.button);
        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        isLogged = false; // Only for pre initializing
        savedUserData = getSharedPreferences("savedUserData", MODE_PRIVATE);
        SharedPreferences.Editor dataWriter = savedUserData.edit();

        String savedEmail = savedUserData.getString("email", "");
        String savedPassword = "";
        if(savedEmail.length() == 0){
            isLogged= false;
        } else {
            savedPassword = savedUserData.getString("email", "");
            isLogged = true;
        }

        setLoginStatus(savedEmail, savedPassword);

        loginButton.setOnClickListener(view -> {
            if(!isLogged) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if(email.length() == 0) {
                    emailText.setError("The Email is Required");
                    return;
                }
                if(password.length() == 0) {
                    passwordText.setError("The Password is Required");
                    return;
                }

                dataWriter.putString("email", email);
                dataWriter.putString("password", password);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Iniciar nueva actividad
                                    Intent userActivity = new Intent(MainActivity.this, UserActivity.class);
                                    startActivity(userActivity);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // mostrar error
                                }
                            }
                        });

            } else {
                dataWriter.clear();
            }
            dataWriter.apply();


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            // Obtener informacion del usuario e iniciar la actividad correspondiente
            Intent userActivity = new Intent(MainActivity.this, UserActivity.class);
            startActivity(userActivity);
        }

        // Verificar SharedPreferences

    }
}