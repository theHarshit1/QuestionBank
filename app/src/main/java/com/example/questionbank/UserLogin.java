package com.example.questionbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_screen);

        auth = FirebaseAuth.getInstance();

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        emailField = (EditText) findViewById(R.id.editTextEmail);
        passwordField = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Enter email",
                            Toast.LENGTH_SHORT).show();

                } else if (password.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Enter password",
                            Toast.LENGTH_SHORT).show();

                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(UserLogin.this, AdminDashboard.class));

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(UserLogin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}
