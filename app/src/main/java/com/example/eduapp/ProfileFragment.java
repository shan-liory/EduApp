package com.example.eduapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eduapp.MainActivity;
import com.example.eduapp.MainViewModel;
import com.example.eduapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProfileFragment extends BaseFragment {


    TextView text_profile, text_dob;
    Button logout_btn;
    ImageView profile_image;
    ImageButton edit_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout_btn= view.findViewById(R.id.logout_btn);
        text_profile=view.findViewById(R.id.text_profile);
        profile_image = view.findViewById(R.id.profile_image);
        text_dob = view.findViewById(R.id.text_dob);
        edit_btn = view.findViewById(R.id.edit_button);

        text_profile.setText(mainViewModel.getTest());
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_editProfileFragment);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();


        DocumentReference documentReference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        documentReference = firestore.collection("User").document(currentId);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String urlResult = task.getResult().getString("url");
                            String dobResult = task.getResult().getString(("dob"));

                            Picasso.get().load(urlResult).into(profile_image);
                            text_profile.setText(nameResult);
                            text_dob.setText((dobResult));


                        }else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void logoutUser() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setTitle("Exit Application?");
//        alertDialogBuilder
//                .setMessage("Confirm Signing Out?")
//                .setCancelable(false)
//                .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                FirebaseAuth.getInstance().signOut();
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
//
//                            }
//                        })
//
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
        FirebaseAuth.getInstance().signOut();
        Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_welcomeActivity);
        ((MainActivity)requireActivity()).setBottomNavVisibility(false);

    }
    //check streak should be called upon app loading to check if last day played was >=2 days ago
    public void checkStreak(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference doc = db.collection("User").document(currentUserId);


        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Calendar c = Calendar.getInstance();
                        int thisDay = c.get((Calendar.DAY_OF_YEAR));
                        int lastDay;

                        //get lastdayfield from firestore document
                        lastDay = Integer.parseInt(document.getString("lastStreakDay"));
                        int counterOfConsecutiveDays = Integer.parseInt(document.getString("consecutiveStreakDays"));
                        //if last day played was yesterday


                        if (lastDay <= (thisDay -2)) {
                            counterOfConsecutiveDays = 0;
                            writeIntoDatabaseStreakDays(counterOfConsecutiveDays);
                        }
                    } else {
                        Log.d("bobo", "No such document");
                    }
                } else {
                    Log.d("bobo", "get failed with ", task.getException());
                }
            }
        });

    }

    //add streak is just to +1 into streak days
    public void addStreak(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference doc = db.collection("User").document(currentUserId);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("bobo", "Document Exists");
                        Calendar c = Calendar.getInstance();
                        int thisDay = c.get((Calendar.DAY_OF_YEAR));
                        int lastDay;

                        //get lastdayfield from firestore document
                        lastDay = Integer.parseInt(document.getString("lastStreakDay"));
                        int counterOfConsecutiveDays = Integer.parseInt(document.getString("consecutiveStreakDays"));
                        //if last day played was yesterday

                        //if they played yesterday, add 1
                        if (lastDay != thisDay){

                            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;
                            writeIntoDatabaseStreakDays(counterOfConsecutiveDays);
                            writeIntoDatabaseLastDay(thisDay);
                        } // if they played today, do nothing
                        else if (lastDay == thisDay){
                            //do nothing
                        }
                    } else {
                        Log.d("bobo", "No such document");
                    }
                } else {
                    Log.d("bobo", "get failed with ", task.getException());
                }
            }
        });


    }

    private void writeIntoDatabaseStreakDays(int counterOfConsecutiveDays){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("User").document(currentUserId);


        doc
            .update("consecutiveStreakDays", String.valueOf(counterOfConsecutiveDays))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("dodo", "DocumentSnapshot successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("dodo", "Error updating document", e);
                }
            });

    }

    public void writeIntoDatabaseLastDay(int lastDay){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("User").document(currentUserId);

        doc
                .update("lastStreakDay", String.valueOf(lastDay))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dodo", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dodo", "Error updating document", e);
                    }
                });
    }
}

