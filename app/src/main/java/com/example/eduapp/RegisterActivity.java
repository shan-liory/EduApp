package com.example.eduapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    TextView haveAccount;
    EditText editText_name_register, editText_dob_register, editText_email_register, editText_password_register;
    ImageView register_back_btn;
    Button register_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        haveAccount = (TextView) findViewById(R.id.haveAccount_text);
        editText_name_register = (EditText) findViewById(R.id.editText_name_register);
        editText_dob_register = (EditText) findViewById(R.id.editText_dob_register);
        editText_email_register = (EditText) findViewById(R.id.editText_email_register);
        editText_password_register = (EditText) findViewById(R.id.editText_password_register);
        register_back_btn = (ImageView) findViewById(R.id.register_back_btn);
        register_btn = (Button) findViewById(R.id.register_btn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AuthenticateUser();
            }
        });

    }

    private void AuthenticateUser() {
        String email = editText_email_register.getText().toString();
        String name = editText_name_register.getText().toString();
        String dob = editText_dob_register.getText().toString();
        String password = editText_password_register.getText().toString();


        if (!email.matches(regex)) {
            editText_email_register.setError("Enter valid email");
        } else if (password.isEmpty() || password.length() < 6) {
            editText_password_register.setError("Enter proper password");
        }
       /* else if(dob.isEmpty()){
            editText_dob_register.setError("Enter your Date of Birth");
        }
        else if(name.isEmpty()){
            editText_dob_register.setError("Enter your name");
        }*/
        else {
            progressDialog.setMessage("Please wait for registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
