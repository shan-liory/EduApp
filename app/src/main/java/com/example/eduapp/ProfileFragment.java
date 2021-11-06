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


    public void updateStreak(){
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
                        Log.d("bobo",String.valueOf(thisDay));
                        Log.d("bobo", String.valueOf(lastDay));

                        if (lastDay == (thisDay-1) || lastDay == thisDay){
                            Log.d("bobo", "Win win");
                            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;
                            lastDay = thisDay;
                        } else {
                            Log.d("bobo","Streak ended");
                            counterOfConsecutiveDays = 0;
                            lastDay = 0;
                        }
                        writeIntoDatabaseStreak(counterOfConsecutiveDays,lastDay);
                    } else {
                        Log.d("bobo", "No such document");
                    }
                } else {
                    Log.d("bobo", "get failed with ", task.getException());
                }
            }
        });


    }

    private void writeIntoDatabaseStreak(int counterOfConsecutiveDays, int lastDay){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("User").document(currentUserId);
        Log.d("zozo", String.valueOf(counterOfConsecutiveDays));

        doc
            .update("consecutiveStreakDays", counterOfConsecutiveDays)
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
        doc
            .update("lastStreakDay", lastDay)
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

// database, there is consecutive days field, and there is last played date field
// first initialzie last played date to 0
// get current day
// do checking - for consecutive days as well
// update db with new last played date, and consecutive days


//ideally i'd like to add 2 fields - lastDay (last played date) & consecutivedays (streak days)
// these 2 ^ initialize inside register fragment
// when user completes a game, do updatestreak (which will do checking inside the function)
// check streak method is just to retrieve lastDay from db & count consecutive days
// update streak method shld be just to overwrite db
// update streak method shld only be called when user finishes game

//need to initialize in db int counterOfConsecutiveDays++;
//if last day is yesterday, then add one into the counter        if (lastDay == thisDay-1){
////                //CONSECUTIVE DAYS
////                counterOfConsecutiveDays = counterOfConsecutiveDays +1;
////                writeIntoDatabaseStreak(counterofConsecutiveDays);
////                }
////                //if not, then reset counter
////                else {
////                counterOfConsecutiveDays = 0;
////                writeIntoDatabaseStreak(counterOfConsecutiveDays);
////                }
////                //whut am i doing
////                //whut is this??
////                transaction.update(doc, "lastStreakDay", thisDay);
//