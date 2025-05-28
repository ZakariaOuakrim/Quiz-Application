package com.zakaria.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registerActivity extends AppCompatActivity {

    EditText etMail, etPassword, etPassword1, etUsername;
    Button bRegister;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etMail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etPassword1 = findViewById(R.id.confirmPassword);
        etUsername = findViewById(R.id.username);
        bRegister = findViewById(R.id.button);

        myAuth = FirebaseAuth.getInstance();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String password1 = etPassword1.getText().toString().trim();
                String username = etUsername.getText().toString().trim();

                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please enter a username",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mail)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Please confirm your password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password1)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(mail, password, username);
            }
        });
    }

    private void registerUser(String email, String password, final String username) {
        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success, update user profile with username
                            FirebaseUser user = myAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(registerActivity.this,
                                                        "Registration successful!",
                                                        Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(registerActivity.this, MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(registerActivity.this,
                                                        "Failed to set username: " + task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            // Registration failed
                            Toast.makeText(registerActivity.this,
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}