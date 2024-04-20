package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button signInButton;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                    checkUser();
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateUsername() {
        String username = usernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            usernameEditText.setError("Username cannot be empty");
            return false;
        } else {
            usernameEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordEditText.setError("Password cannot be empty");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = usernameEditText.getText().toString().trim();
        String userPassword = passwordEditText.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameEditText.setError(null);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String passwordFromDB = ds.child("password").getValue(String.class);
                        if (passwordFromDB.equals(userPassword)) {
                            passwordEditText.setError(null);

                            Intent intent = new Intent(Signin.this, Menu.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    passwordEditText.setError("Invalid Credentials");
                    passwordEditText.requestFocus();
                } else {
                    usernameEditText.setError("User does not exist");
                    usernameEditText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}