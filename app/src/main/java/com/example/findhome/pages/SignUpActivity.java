package com.example.findhome.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findhome.R;
import com.example.findhome.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView haveAccount;
    private FirebaseAuth mAuth;
    private EditText userName, userEmail, userPassword, confirmPassword;
    private Button signUpButton;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        haveAccount = findViewById(R.id.have_account);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.user_confirm_password);
        signUpButton = findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
//
//        haveAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
        //NEW
        signUpButton.setOnClickListener(view -> {
            createUser();
        });

        haveAccount.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }

        private void createUser(){
            String name = userName.getText().toString();
            String email = userEmail.getText().toString();
            String password = userPassword.getText().toString();
            String confirm_password = confirmPassword.getText().toString();

            if (TextUtils.isEmpty(email)){
                userEmail.setError("Email cannot be empty");
                userEmail.requestFocus();
            }else if (TextUtils.isEmpty(password)){
                userPassword.setError("Password cannot be empty");
                userPassword.requestFocus();
            }else if (TextUtils.isEmpty(confirm_password)){
                userPassword.setError("Confirm your password");
                userPassword.requestFocus();
            }else{
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = new User(name,email);
                        if (task.isSuccessful()){
                            mRef.child("users").push().setValue(user);
                            Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(SignUpActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        //NEW DONE

//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(userName.getText().toString().trim().isEmpty()){
//                    Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
//                }else if(userEmail.getText().toString().trim().isEmpty()){
//                    Toast.makeText(SignUpActivity.this,"Enter valid email", Toast.LENGTH_SHORT).show();
//                }else if(userPassword.getText().toString().trim().isEmpty()){
//                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
//                }else if(!userPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
//                    Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
//                }else{
//                    if (emailChecker(userEmail.getText().toString().trim())){
//                        createUser(userEmail.getText().toString().trim(),
//                                userPassword.getText().toString().trim(),
//                                userName.getText().toString().trim());
//                    }else {
//                        Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
    }

//    boolean emailChecker(String email){
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }

//    void createUser(String email, String password, String name){
//        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                User user = new User(name,email);
//
//                if(task.isSuccessful()){
//                    mRef.child("users").push().setValue(user);
//                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
//                    finish();
//
//                    Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignUpActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }