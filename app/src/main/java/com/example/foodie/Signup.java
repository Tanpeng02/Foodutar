package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    TextView alreadyHaveAccountTextView;
    Button signUpButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        alreadyHaveAccountTextView = findViewById(R.id.alreadyHaveAccountTextView);
        signUpButton = findViewById(R.id.signInButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields()) {
                    return;
                } else {
                    signUpUser();
                }
            }
        });

        alreadyHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Signin.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(Signup.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Signup.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signUpUser() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        HelperClass helperClass = new HelperClass(email, username, password);
        reference.child(username).setValue(helperClass);

        Toast.makeText(Signup.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Signup.this, Signin.class);
        startActivity(intent);
    }
}

