package com.example.eduapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    EditText login_email, login_password;
    Button login_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    ImageView login_back_btn;
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

    MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        login_back_btn = view.findViewById(R.id.login_back_btn);
        login_btn = view.findViewById(R.id.login_btn);

        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        login_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
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


        if (!email.matches(regex)) {
            login_email.setError("Enter valid email");
        } else if (password.isEmpty() || password.length() < 6) {
            login_password.setError("Enter proper password");
        }
       /* else if(dob.isEmpty()){
            editText_dob_register.setError("Enter your Date of Birth");
        }
        else if(name.isEmpty()){
            editText_dob_register.setError("Enter your name");
        }*/
        else {
            progressDialog.setMessage("Please wait for login");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                              @Override
                                              public void onComplete(@NonNull Task<AuthResult> task) {
                                                  if (task.isSuccessful()) {
                                                      progressDialog.dismiss();
                                                      //here is where u will query for user data after login
//                                                      User user = new User();
//                                                      user.setPassword("mypassword");
//                                                      mainViewModel.setUser(user);
                                                      sendUserToNextActivity();
                                                      Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                  } else {
                                                      progressDialog.dismiss();
                                                      Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          }
                    );
        }


    }

    private void sendUserToNextActivity() {
        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_navigation_home);
    }

}
