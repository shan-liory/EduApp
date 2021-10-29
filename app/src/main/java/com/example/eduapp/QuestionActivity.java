package com.example.eduapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    Button confirm;
    String rightAnswer;
    String Answer;
    List<Question> questions = new ArrayList<Question>();
    int score;

    ArrayList<String> courses = new ArrayList<String>() {
        {
            add("Abstraction");
            add("Algorithmic Thinking");
            add("Decomposition");
            add("Pattern Recognition");
        }
    };

    ArrayList<String> lessons = new ArrayList<String>() {
        {
            add("Lesson 1");
            add("Lesson 2");
        }
    };


    String correctAns = "unchanged";
    String questionImage = null;
    ArrayList<String> wrongAns = new ArrayList<String>();

//    PreloadQuestions loadDBQuestions = new PreloadQuestions();

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
        optionA = findViewById(R.id.opcaoA);
        optionB = findViewById(R.id.opcaoB);
        optionC = findViewById(R.id.opcaoC);
        optionD = findViewById(R.id.opcaoD);
        score = 0;
        radioGroup = findViewById(R.id.radioGroup);

        //put into array
        all = new int[]{R.id.opcaoA,R.id.opcaoB,R.id.opcaoC,R.id.opcaoD};

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
//        loadDBQuestions.execute();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Abstraction").child("Lesson 1").child("Q1").child("Correct Choice");
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(ArrayList<Question> dataSnapshotValue) {

                questions = dataSnapshotValue;
                Log.d("lolo", String.valueOf(questions.size()));
                loadQuestion();
            }
        });


    }


//    protected class PreloadQuestions extends AsyncTask<Void, Void, String> {
//
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            for (String course : courses) {
//                for (String lesson : lessons) {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(course).child(lesson).child("Q1");
//
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                if (snapshot.getKey().equals("Correct Choice")) {
//                                    correctAns = snapshot.getValue().toString();
//
//                                } else if (snapshot.getKey().equals("Question Image")) {
//                                    questionImage = snapshot.getValue().toString();
//
//                                } else if (snapshot.getKey().equals("Wrong Choice")) {
//                                    //need to randomize answer location
//                                    wrongAns.add(correctAns);
//
//                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                        wrongAns.add(snapshot1.getValue().toString());
//                                    }
//                                }
//                            }
//
////                            Log.d("lolo", "Correct " + correctAns);
////                            Log.d("lolo", "QI " + questionImage);
////                            Log.d("lolo", course + " " + lesson);
//                            Question question = new Question("Question", correctAns, wrongAns);
//                            q.add(question);
//                            Log.d("lolo", String.valueOf(q.size()) + "added");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                    Log.d("lolo", String.valueOf(q.size()) + "iteration");
//                }
//            }
//            Log.d("lolo", String.valueOf(q.size()) + "before return");
//            return correctAns;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("lolo", result + "final");
//            //loadQuestion();
//        }
//    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(ArrayList<Question> dataSnapshotValue);
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ArrayList<Question> q = new ArrayList<Question>();
        for (String course : courses) {
            for (String lesson : lessons) {
                DatabaseReference r = FirebaseDatabase.getInstance().getReference().child(course).child(lesson).child("Q1");

                r.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getKey().equals("Correct Choice")) {
                                    correctAns = snapshot.getValue().toString();

                                } else if (snapshot.getKey().equals("Question Image")) {
                                    questionImage = snapshot.getValue().toString();

                                } else if (snapshot.getKey().equals("Wrong Choice")) {
                                    //need to randomize answer location
                                    wrongAns.add(correctAns);

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        wrongAns.add(snapshot1.getValue().toString());
                                    }
                                }
                            }

//                            Log.d("lolo", "Correct " + correctAns);
//                            Log.d("lolo", "QI " + questionImage);
//                            Log.d("lolo", course + " " + lesson);
                            Question question = new Question("Question", correctAns, wrongAns);
                            q.add(question);
                        // This is how you use the value once it is loaded! Make sure to pass the
                        // value of the DataSnapshot object, not the object itself (this was the
                        // original answerer's mistake!
                        Log.d("lolo", String.valueOf(q.size()) + " adding");
                        if (q.size() == 8) {
                            listener.onSuccess(q);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        loadQuestion();
    }

    private void loadQuestion(){
        Log.d("lolo", "load ran");
        if(questions.size() > 0) {
            Question q = questions.remove(0);
            lblQuestion.setText(q.getQuestion());
            List<String> answers = q.getAnswers();

            optionA.setText(answers.get(0));
            optionB.setText(answers.get(1));
            optionC.setText(answers.get(2));
            optionD.setText(answers.get(3));

            rightAnswer = q.getRightAnswer();

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

    public void loadAnswer(View view) {
        int op = radioGroup.getCheckedRadioButtonId();

        switch (op){
            case R.id.opcaoA:
                Answer="0";
                break;

            case R.id.opcaoB:
                Answer="1";
                break;

            case R.id.opcaoC:
                Answer="2";
                break;

            case R.id.opcaoD:
                Answer="3";
                break;

            default:
                return;

        }

        radioGroup.clearCheck();

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
