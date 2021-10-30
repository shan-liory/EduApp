package com.example.eduapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class QuestionActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    TextView lblQuestion;
    ImageView optionA;
    ImageView optionB;
    ImageView optionC;
    ImageView optionD;
    Button confirm;
    String rightAnswer;
    String Answer;
    List<Question> questions = new ArrayList<Question>();
    int score;

    Intent intent;
    String course;
    String lesson;
    String questionText = null;
    String correctAns = null;
    String questionImage = null;
    ArrayList<String> answers = new ArrayList<String>();

    //test hints
    Button mHintButton;
    Random random;
    int[] all;
    private int mRemainingHints = 2;
    Set except;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //test hints
        random = new Random();
        except = new HashSet();

        mHintButton = (Button)findViewById(R.id.hint_btn);
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.hint_btn) {
                    int rand;

                    do {
                        rand = random.nextInt(all.length);
                        //Log.d("RAND GEN:",String.valueOf(rand));
                    } while (except.contains(rand) || rand == Integer.parseInt(rightAnswer));

                    except.add(rand);
                    //Log.d("EXCEPT VALUE:",except.toString());

                    int id = all[rand];
                    findViewById(id).setEnabled(false);

                    //limit to 2 hints
                    mRemainingHints--;
                    setHintButton(true);
                }
            }
        });

        //og code
        confirm = findViewById(R.id.confirm);
        lblQuestion = findViewById(R.id.lblPergunta);
        optionA = findViewById(R.id.choice1);
        optionB = findViewById(R.id.choice2);
        optionC = findViewById(R.id.choice3);
        optionD = findViewById(R.id.choice4);
        score = 0;

        //put into array
        all = new int[]{R.id.choice1,R.id.choice2,R.id.choice3,R.id.choice4};

//        questions = new ArrayList<Question>(){
//            {
////                add(new Question("Quanto que vale o número de Euler?", "2", "3.1415", "1.7189","2.7189", "5.985"));
////                add(new Question("Quem foi que disse a seguinte frase: \"Ame o seu próximo como a si mesmo\"?", "0", "Jesus", "Hitler","Mussolini", "Stalin"));
////                add(new Question("Quem é Friedrich Nietzsche?", "3", "Pedreiro", "Programador","Músico", "Filósofo"));
////                add(new Question("A música \"Billie Jean\" é cantada por quem?", "1", "Whindersson Nunes", "Michael Jackson", "Kanye West", "Billie Eilish"));
////                add(new Question("O que é um Ukulele?", "0", "Instrumento Musical", "Comida", "Empresa", "Time de Futebol"));
////                add(new Question("Em tecnologia, o que é I.A?", "3", "Software", "Sistema Operacional", "Compilador", "Interligência Artificial"));
////                add(new Question("Quanto vale 8 bits?", "2", "1 Bit", "16 Bytes", "1 Byte", "1 Mega Byte"));
////                add(new Question("O que é Bitcoin?", "1", "Moeda governamental", "Crypto Moeda", "Uma rede decentralizada", "Software de Datamining"));
////                add(new Question("Quem foi que criou o Bitcoin?", "1", "Margaret Hamilton", "Satoshi Nakamoto", "Alan Turing", "Gustavo Guanabara"));
////                add(new Question("Quem foi o primeiro programador?", "3", "Steve Jobs", "Linus Torvalds", "Alan Turing", "Ada Lovelace"));
//            }
//        };

        intent = getIntent();
        course = intent.getStringExtra("Course");
        lesson = intent.getStringExtra("Lesson");
        Log.d("olo", course + lesson);

        //This ref is not used, it is only used as input due to parameter requirements.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Abstraction").child("Lesson 1").child("Q1").child("Correct Choice");
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(ArrayList<Question> dataSnapshotValue) {

                questions = dataSnapshotValue;

                for (Question q : questions) {
                    for (String s : q.getAnswers()) {
                        Log.d("lolo", s);
                    }
                    Log.d("lolo", "img " + q.getQuestion());
                }
                Log.d("lolo", String.valueOf(questions.size()));
                loadQuestion();
            }
        });


    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(ArrayList<Question> dataSnapshotValue);
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ArrayList<Question> q = new ArrayList<Question>();

        // Use this way when need to load according to courses and lessons.
//      DatabaseReference r = FirebaseDatabase.getInstance().getReference().child(course).child(lesson).child("Q1");
//        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Question");
//
//        r.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot course : dataSnapshot.getChildren()) {
//                    for (DataSnapshot lesson : course.getChildren()) {
//                        answers = new ArrayList<String>();
//                        for (DataSnapshot questionInfo : lesson.child("Q1").getChildren()) {
//                            Log.d("lololo", "loop 3");
//                            if (questionInfo.getKey().equals("Question Text")) {
//                                questionText = questionInfo.getValue().toString();
//
//                            } else if (questionInfo.getKey().equals("Correct Choice")) {
//                                correctAns = questionInfo.getValue().toString();
//
//                            } else if (questionInfo.getKey().equals("Question Image")) {
//                                questionImage = questionInfo.getValue().toString();
//
//                            } else if (questionInfo.getKey().equals("Answers")) {
//                                for (DataSnapshot ans : questionInfo.getChildren()) {
//                                    answers.add(ans.getValue().toString());
//                                    Log.d("answers", ans.getValue().toString());
//                                }
//                            }
//                        }
//
//                        Question question = new Question(questionText, questionImage, correctAns, answers);
//                        q.add(question);
//                    }
//                }
//
//                for (Question qu : q) {
//                    for (String s : qu.getAnswers()) {
//                        Log.d("lolololo", s);
//                    }
//                }
////                if (q.size() == 8) {
////                    listener.onSuccess(q);
////                }
//                listener.onSuccess(q);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Question").child(course).child(lesson).child("Q1");

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                answers = new ArrayList<String>();

                for (DataSnapshot questionInfo : dataSnapshot.getChildren()) {
                    if (questionInfo.getKey().equals("Question Text")) {
                        questionText = questionInfo.getValue().toString();

                    } else if (questionInfo.getKey().equals("Correct Choice")) {
                        correctAns = questionInfo.getValue().toString();

                    } else if (questionInfo.getKey().equals("Question Image")) {
                        questionImage = questionInfo.getValue().toString();

                    } else if (questionInfo.getKey().equals("Answers")) {
                        for (DataSnapshot ans : questionInfo.getChildren()) {
                            answers.add(ans.getValue().toString());
                            Log.d("answers", ans.getValue().toString());
                        }
                    }
                    Log.d("olo", "inside");
                }

                Question question = new Question(questionText, questionImage, correctAns, answers);
                q.add(question);


                for (Question qu : q) {
                    for (String s : qu.getAnswers()) {
                        Log.d("lolololo", s);
                    }
                }

                listener.onSuccess(q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        loadQuestion();
    }

    private void loadQuestion(){
        Log.d("lolo", "load ran");
        if(questions.size() > 0) {
            Question displayQuestion = questions.remove(0);
            lblQuestion.setText(displayQuestion.getQuestion());
            List<String> answers = displayQuestion.getAnswers();

            Picasso.get().load(displayQuestion.getQuestionImage()).into((ImageView) findViewById(R.id.questionImage));
            Picasso.get().load(answers.get(0)).into((ImageView) findViewById(R.id.choice1));
            Picasso.get().load(answers.get(1)).into((ImageView) findViewById(R.id.choice2));
            Picasso.get().load(answers.get(2)).into((ImageView) findViewById(R.id.choice3));
            Picasso.get().load(answers.get(3)).into((ImageView) findViewById(R.id.choice4));

            rightAnswer = displayQuestion.getRightAnswer();

        } else {
            Intent intent = new Intent(this, ShowScoreActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
        mRemainingHints = 2;
        except = new HashSet();
        setHintButton(true);

        // setButton crashes because index error when it tries to remove from empty array.
//        setButton(true);
    }

    public void selectChoice(View view) {
        int op = view.getId();

        switch (op){
            case R.id.choice1:
                Answer="0";
                break;

            case R.id.choice2:
                Answer="1";
                break;

            case R.id.choice3:
                Answer="2";
                break;

            case R.id.choice4:
                Answer="3";
                break;

            default:
                return;

        }

    }

    public void loadAnswer(View view) {
        this.startActivity(isRightOrWrong(Answer));
    }



    private Intent isRightOrWrong(String Answer){
        Intent screen;
        if(Answer.equals(rightAnswer)) {
            this.score += 1;
            screen = new Intent(this, RightActivity.class);

        }else {
            screen = new Intent(this, WrongActivity.class);
        }

        return screen;
    }

    //set button to enable or disable
    private void setButton(boolean b) {
        Question q = questions.remove(0);
        if (q.getAnswered() > 0 || b == false) {
            //disable buttons
            optionA.setEnabled(false);
            optionB.setEnabled(false);
            optionC.setEnabled(false);
            optionD.setEnabled(false);
        } else {
            optionA.setEnabled(true);
            optionB.setEnabled(true);
            optionC.setEnabled(true);
            optionD.setEnabled(true);
        }
    }

    private void setHintButton(boolean b) {
        if (mRemainingHints == 0) {
            mHintButton.setEnabled(false);
        } else {
            mHintButton.setEnabled(true);
        }
    }
}


