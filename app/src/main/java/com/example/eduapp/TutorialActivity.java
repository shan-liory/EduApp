package com.example.eduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Locale;

public class TutorialActivity extends AppCompatActivity {

    Intent intent;
    String course;
    String lesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        intent = getIntent();
        course = intent.getStringExtra("Course");
        lesson = intent.getStringExtra("Lesson");

        String source = String.format("@drawable/" + course + " " + lesson);
        source = source.replaceAll("\\s+","_");
        source = source.toLowerCase();
        ((ImageView) findViewById(R.id.gifImageView3)).setImageURI(Uri.parse(source));
    }

    public void onStartClick(){
        Intent i = new Intent(this, QuestionActivity.class);

        i.putExtra("Course", course);
        i.putExtra("Lesson", lesson);
        startActivity(i);
    }

    public void goToHome(View v){
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
    }


}