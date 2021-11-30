package com.example.superanalitico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences savedUserData;
    boolean isLogged;
    EditText emailText;
    EditText passwordText;
    Button loginButton;

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
                setLoginStatus(email, password);

            } else {
                dataWriter.clear();
            }
            dataWriter.apply();


        });
    }
}