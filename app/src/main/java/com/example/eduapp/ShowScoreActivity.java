package com.example.eduapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShowScoreActivity extends AppCompatActivity {
    TextView TxtScore;
    TextView TxtStatus;
    MediaPlayer audio;
    ImageButton share_btn;
    Intent currentLesson;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);
        TxtScore = findViewById(R.id.txtscore);
        TxtStatus = findViewById(R.id.txtStatus);
        share_btn = findViewById(R.id.share_btn);
        constraintLayout = findViewById(R.id.constraintLayout);
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
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HTTP", "hello2");
                share(screenShot(constraintLayout));
            }

        });

    }

    private String setStatus(String scores){
        int score = Integer.parseInt(scores);
        if(score >= 8){
            audio = MediaPlayer.create(this, R.raw.high_score);
            return "Você é muito inteligente!";
        }

        if (score >= 1){
            audio = MediaPlayer.create(this,  R.raw.medium_score);
            return "Well Done!";
        }

        audio = MediaPlayer.create(this,  R.raw.low_score);
        return "You can do better!";

    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        Log.d("HTTP", "hello");
        return bitmap;


    }

    public void share(Bitmap bitmap){
        String pathofBmp=
                MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        bitmap,"title", null);
        Uri uri = Uri.parse(pathofBmp);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KidoozeApp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        this.startActivity(Intent.createChooser(shareIntent, "My Score!"));
    }
    public void goToHome(View v){
      onBackPressed();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
