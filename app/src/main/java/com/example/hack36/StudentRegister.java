package com.example.hack36;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import java.util.regex.Pattern;

public class StudentRegister extends AppCompatActivity {
    EditText student_register_email,student_register_password;
    Button student_register_button;
    TextView student_register_sign_in;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        student_register_email = (EditText) findViewById(R.id.student_register_email);
        student_register_password = (EditText) findViewById(R.id.student_register_password);
        student_register_button = (Button) findViewById(R.id.student_register_button);
        student_register_sign_in = (TextView) findViewById(R.id.student_register_sign_in);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User..");

        student_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = student_register_email.getText().toString().trim();
                String password = student_register_password.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    student_register_email.setError("Invalid Email");
                    student_register_email.setFocusable(true);
                }
                else if(password.length()<6)
                {
                    student_register_password.setError("Password length atleast 6");
                    student_register_password.setFocusable(true);
                }
                else
                {
                    registerUser(email,password);
                }
            }
        });

        student_register_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentRegister.this,StudentLogin.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(StudentRegister.this, "Successfully Registered.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudentRegister.this,ProfileActivity.class);
                            startActivity(intent);
                            finish();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(StudentRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
