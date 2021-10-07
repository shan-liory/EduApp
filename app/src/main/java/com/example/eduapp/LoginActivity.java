package com.example.eduapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText login_email, login_password;
    Button login_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    ImageView login_back_btn;
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_back_btn = (ImageView) findViewById(R.id.login_back_btn);
        login_btn = (Button) findViewById(R.id.login_btn);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        login_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }


        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email = login_email.getText().toString();

        String password = login_password.getText().toString();



        if (!email.matches(regex))
        {
            login_email.setError("Enter valid email");
        }else if(password.isEmpty() || password.length() <6 )
        {
            login_password.setError("Enter proper password");
        }
       /* else if(dob.isEmpty()){
            editText_dob_register.setError("Enter your Date of Birth");
        }
        else if(name.isEmpty()){
            editText_dob_register.setError("Enter your name");
        }*/
        else{
            progressDialog.setMessage("Please wait for registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();
        }


    }


}

