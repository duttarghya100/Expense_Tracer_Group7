package com.example.expense_tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    final String TAG="Expense Tracer";
    private EditText mEmail;
    private EditText mpass;
    private Button btnLogin;
    private TextView mSignupHere;
    private TextView mforgotPassword;

    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        loginDetails();
    }
    private void loginDetails()
    {
        mEmail=findViewById(R.id.email_login);
        mpass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        mSignupHere=findViewById(R.id.signup_here);
        mforgotPassword=findViewById(R.id.forgot_password);
    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mDialog.dismiss();
                        try {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Log.d(TAG,ex.getMessage());
                        }




                    }
                    else
                    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    });
        //Setting up for Registration Activity
        mSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
        mforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
            }
        });

    }

}