package com.example.expense_tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mpass;
    private Button btnReg;
    private TextView mSignin;

    private ProgressDialog mDialog;
    //Firebase Authentication
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        Registration();
    }
    private void   Registration()
    {
        mEmail=findViewById(R.id.email_signup);
        mpass=findViewById(R.id.password_signup);
        mSignin=findViewById(R.id.signup_here);
        btnReg=findViewById(R.id.btn_signup);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegistrationActivity.this, "Yo yo", Toast.LENGTH_SHORT).show();
                String email=mEmail.getText().toString().trim();
                String password=mpass.getText().toString().trim();
                if (TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is required..");
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    mpass.setError("Password is required..");
                    return;
                }
                mDialog.setMessage("Processing..");
                mDialog.show();

    // Authentication for email and password
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            mDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Resigtraion Successful...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Resigtraion Failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}