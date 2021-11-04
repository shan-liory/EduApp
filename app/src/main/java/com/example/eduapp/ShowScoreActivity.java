package com.example.eduapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowScoreActivity extends AppCompatActivity {
    TextView TxtScore;
    TextView TxtStatus;
    MediaPlayer audio;
    Intent currentLesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);
        TxtScore = findViewById(R.id.txtscore);
        TxtStatus = findViewById(R.id.txtStatus);
        Intent intent = getIntent();
        String scores = String.valueOf(intent.getIntExtra("score", 0));
        String course = intent.getStringExtra("Course");
        String lesson = intent.getStringExtra("Lesson");
        currentLesson = new Intent(this, QuestionActivity.class);
        currentLesson.putExtra("Course", course);
        currentLesson.putExtra("Lesson", lesson);

        TxtScore.setText(scores);
        TxtStatus.setText(setStatus(scores));
        audio.start();
    }

    private String setStatus(String scores){
        int score = Integer.parseInt(scores);

        if(score >= 8){
            audio = MediaPlayer.create(this, R.raw.high_score);
            return "Você é muito inteligente!";
        }

        if (score >= 5){
            audio = MediaPlayer.create(this,  R.raw.medium_score);
            return "Parabéns!";
        }

        audio = MediaPlayer.create(this,  R.raw.low_score);
        return "Você precisa estudar mais...";

    }


    public void goToHome(View v){
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
    }


    public void tryAgain(View v) {
        //Destroy activity to remove it from back stack.
        //Otherwise, the complete() will move back to the prev score if they try again.
        startActivity(currentLesson);
        finish();
    }

    public void complete(View v) {
        this.onBackPressed();
    }
}
