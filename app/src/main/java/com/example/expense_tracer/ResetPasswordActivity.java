package com.example.expense_tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPAsswordButton;
    private ProgressBar progressBar;
    FirebaseAuth  auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText=findViewById(R.id.email_signup);
        resetPAsswordButton=findViewById(R.id.btn_signup);
        progressBar=findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();

        resetPAsswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email=emailEditText.getText().toString().trim();
        if (email.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Please provide a valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Check Your Email to Reset Password!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ResetPasswordActivity.this, "Try Again! Something Wrong happened!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}