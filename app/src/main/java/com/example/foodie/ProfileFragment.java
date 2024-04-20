package com.example.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    EditText editUsername, editEmail, editPassword, editConfirmPassword;
    Button saveButton;
    String username, email, password, confirmPassword;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editUsername = rootView.findViewById(R.id.editUsername);
        editEmail = rootView.findViewById(R.id.editemail);
        editPassword = rootView.findViewById(R.id.editpassword);
        editConfirmPassword = rootView.findViewById(R.id.editconfirmpassword);
        saveButton = rootView.findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsernameChanged() || isEmailChanged() || isPasswordChanged() || isConfirmPasswordChanged()) {
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private boolean isUsernameChanged() {
        String newUsername = editUsername.getText().toString().trim();
        if (!newUsername.equals(username)) {
            if (username != null) { // Check if username is not null
                reference.child(username).child("username").setValue(newUsername)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
                                editor.putString("username", newUsername);
                                editor.apply();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to update username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Handle the case where username is null
                Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
            }
            username = newUsername;
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        String newEmail = editEmail.getText().toString().trim();
        if (!newEmail.equals(email)) {
            if (username != null) { // Check if username is not null
                reference.child(username).child("email").setValue(newEmail)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to update email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Handle the case where username is null
                Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
            }
            email = newEmail;
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChanged() {
        String newPassword = editPassword.getText().toString().trim();
        if (!newPassword.equals(password)) {
            if (username != null) { // Check if username is not null
                reference.child(username).child("password").setValue(newPassword)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
                                editor.putString("password", newPassword);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Handle the case where username is null
                Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
            }
            password = newPassword;
            return true;
        } else {
            return false;
        }
    }

    private boolean isConfirmPasswordChanged() {
        String newConfirmPassword = editConfirmPassword.getText().toString().trim();
        if (!newConfirmPassword.equals(confirmPassword)) {
            if (username != null) { // Check if username is not null
                reference.child(username).child("confirmPassword").setValue(newConfirmPassword)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Confirm Password updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to update confirm password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Handle the case where username is null
                Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
            }
            confirmPassword = newConfirmPassword;
            return true;
        } else {
            return false;
        }
    }


    private void showData() {
        if (getArguments() != null) {
            username = getArguments().getString("Username", "");
            email = getArguments().getString("Email", "");
            password = getArguments().getString("Password", "");
            confirmPassword = getArguments().getString("ConfirmPassword", "");
        }
        editUsername.setText(username);
        editEmail.setText(email);
        editPassword.setText(password);
        editConfirmPassword.setText(confirmPassword);
    }
}
