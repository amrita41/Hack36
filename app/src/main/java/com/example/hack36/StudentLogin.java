package com.example.hack36;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentLogin extends AppCompatActivity {
    EditText student_login_email,student_login_password;
    Button student_login_button;
    TextView student_login_sign_in;
    TextView recover_password;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        student_login_email = (EditText) findViewById(R.id.student_login_email);
        student_login_password = (EditText) findViewById(R.id.student_login_password);
        student_login_button = (Button) findViewById(R.id.student_login_button);
        student_login_sign_in = (TextView) findViewById(R.id.student_login_sign_in);
        recover_password = (TextView) findViewById(R.id.student_recover_password);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in..");

        student_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = student_login_email.getText().toString().trim();
                String password = student_login_password.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    student_login_email.setError("Invalid Email");
                    student_login_email.setFocusable(true);
                }
                else if(password.length()<6)
                {
                    student_login_password.setError("Password length atleast 6");
                    student_login_password.setFocusable(true);
                }
                else
                {
                    registerUser(email,password);
                }
            }
        });

        student_login_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLogin.this,StudentRegister.class);
                startActivity(intent);
            }
        });

        recover_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });


    }

    private void showRecoverPasswordDialog() {
        //progressDialog.setMessage("Sending..");
       // progressDialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText email = new EditText(this);
        email.setHint("email");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setMinEms(16);
        linearLayout.addView(email);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mail = email.getText().toString().trim();
                beginRecovery(mail);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
       builder.create().show();
    }

    private void beginRecovery(String mail) {
       mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful())
               {
                   Toast.makeText(StudentLogin.this, "Email sent",
                           Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(StudentLogin.this, "Failed..",
                           Toast.LENGTH_SHORT).show();
               }

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();
               Toast.makeText(StudentLogin.this, e.getMessage(),
                       Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(StudentLogin.this,ProfileActivity.class);
                            startActivity(intent);
                            finish();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(StudentLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
