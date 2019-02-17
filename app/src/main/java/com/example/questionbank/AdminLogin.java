package com.example.questionbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Harshit on 13-11-2018.
 */

public class AdminLogin extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText emailField, passwordField;
    private ProgressBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        auth = FirebaseAuth.getInstance();
        TextView empLogin = (TextView) findViewById(R.id.textViewEmpLogin);
        empLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminLogin.this, UserLogin.class));
                finish();
            }
        });

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        emailField = (EditText) findViewById(R.id.editTextEmail);
        passwordField = (EditText) findViewById(R.id.editTextPassword);
        bar = (ProgressBar) findViewById(R.id.progressBar1);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "Enter email",
                            Toast.LENGTH_SHORT).show();

                } else if (password.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "Enter password",
                            Toast.LENGTH_SHORT).show();

                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(AdminLogin.this, AdminDashboard.class));

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AdminLogin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                bar.setVisibility(View.GONE);

            }
        });
    }
}
