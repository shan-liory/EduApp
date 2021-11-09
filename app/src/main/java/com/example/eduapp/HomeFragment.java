package com.example.eduapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.example.eduapp.User;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private MainViewModel mainViewModel;
    ImageButton guide_btn;
    Button xp_btn;
    Button streaks_btn;
    TextView welcome_text;
    Button lesson_btn;
    Button playground_btn;
    TextView progress_Text;
    ProgressBar progressBar;
    List<String> lessonsCompleted;

    int xp_unicode = 0x1F31F;
    int fire_unicode = 0x1F525;
    String name = "Mike";
    String next_lesson = "Lesson 4: What Comes Next?";
    int num_compLessons = 0;
    int total_lessons = 8;
    int streaks = 0;
    int score = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        checkStreak();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guide_btn = view.findViewById(R.id.guide_btn);
        xp_btn = view.findViewById(R.id.XP_btn);
        streaks_btn = view.findViewById(R.id.streaks_btn);
        welcome_text = view.findViewById(R.id.welcomeText);
        lesson_btn = view.findViewById(R.id.lesson_btn);
        playground_btn = view.findViewById(R.id.playground_btn);
        progress_Text = view.findViewById(R.id.progress_text);
        progressBar = view.findViewById(R.id.progressBar);

        guide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_guideFragment);
            }
        });
        playground_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_notifications);
            }
        });
        lesson_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        streaks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_streaksFragment);
            }
        });
        progressBar.setMax(total_lessons);
        lesson_btn.setText("Start a new lesson?\n\n" + next_lesson + " >>");

        setUpHome();
//        progressBar.setProgress(num_compLessons);
//        progress_Text.setText(num_compLessons + " out of " + total_lessons + " lessons completed");
//        xp_btn.setText(score + " " + new String(Character.toChars(xp_unicode)));
//        streaks_btn.setText(streaks + " " + new String(Character.toChars(fire_unicode)));
//        welcome_text.setText("Welcome, " + name + "!");

    }

    public void setUpHome() {
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
                            String streakResult = task.getResult().getString("consecutiveStreakDays");
                            String scoreResult = task.getResult().getString("score");
                            lessonsCompleted = (List<String>) task.getResult().get("lessonsCompleted");

                            name = String.valueOf(nameResult);
                            streaks = Integer.parseInt(streakResult);
                            score = Integer.parseInt(scoreResult);
                            num_compLessons = lessonsCompleted.size() - 1;

                            welcome_text.setText("Welcome, " + name + "!");
                            streaks_btn.setText(streaks + " " + new String(Character.toChars(fire_unicode)));
                            xp_btn.setText(score + " " + new String(Character.toChars(xp_unicode)));
                            progress_Text.setText(num_compLessons + " out of " + total_lessons + " lessons completed");
                            progressBar.setProgress(num_compLessons);

                        }else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
}