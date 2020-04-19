package com.mingkang.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String SCORE_KEY = "SCORE";
    private final String INDEX_KEY = "INDEX";
    private TextView mTxtQuestion;
    private Button btnTrue;
    private Button btnWrong;
    private int mQuestionIndex;
    private int mQuizQuestion;
    private ProgressBar mProgressBar;
    private TextView mQuizStatsTextView;
    private int mUserScore;


    private QuizModel[] questionCollection = new QuizModel[]{
            new QuizModel(R.string.q1,true),
            new QuizModel(R.string.q2,true),
            new QuizModel(R.string.q3,false),
            new QuizModel(R.string.q4,true),
            new QuizModel(R.string.q5,false),
            new QuizModel(R.string.q6,true),
            new QuizModel(R.string.q7,false),
            new QuizModel(R.string.q8,true),
            new QuizModel(R.string.q9,false),
            new QuizModel(R.string.q10,true),
    };
    final int USER_PROGRESS = (int)Math.ceil(100.0/questionCollection.length);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            mUserScore=savedInstanceState.getInt(SCORE_KEY);
            mQuestionIndex=savedInstanceState.getInt(INDEX_KEY);
        }else{
            mUserScore=0;
            mQuestionIndex=0;
        }
//        Toast.makeText(getApplicationContext(),"OnCreate methods is called", Toast.LENGTH_SHORT).show();
        mTxtQuestion = findViewById(R.id.txtQuestion);
        btnTrue = findViewById(R.id.btnTrue);
        btnWrong = findViewById(R.id.btnWrong);
        mProgressBar = findViewById(R.id.quizProgressBar);
        mQuizStatsTextView = findViewById(R.id.txtQuizStat);

        mQuizStatsTextView.setText(mUserScore+"");

        QuizModel q1 = questionCollection[mQuestionIndex];
        mQuizQuestion = q1.getQuestion();
        mTxtQuestion.setText(mQuizQuestion);

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateUsersAnswer(true);
                changeQuestionOnButtonClick();
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateUsersAnswer(false);
                changeQuestionOnButtonClick();
            }
        });
    }
    private void changeQuestionOnButtonClick(){
        mQuestionIndex = (mQuestionIndex+1)%10;//repeat the value after reaching 9
        if(mQuestionIndex==0){
            AlertDialog.Builder quizAlert = new AlertDialog.Builder(this);
            quizAlert.setCancelable(false);
            quizAlert.setTitle("The quiz is finished");
            quizAlert.setMessage("Your score is "+mUserScore);
            quizAlert.setPositiveButton("Finish the quiz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            quizAlert.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
            quizAlert.show();
        }
        mQuizQuestion = questionCollection[mQuestionIndex].getQuestion();
        mTxtQuestion.setText(mQuizQuestion);
        mProgressBar.incrementProgressBy(USER_PROGRESS);
        mQuizStatsTextView.setText(mUserScore+"");
    }
    private void evaluateUsersAnswer(boolean userGuess){
        boolean currentQuestionAnswer = questionCollection[mQuestionIndex].isAnswer();
        if (userGuess==currentQuestionAnswer) {
            AlertDialog.Builder correctAlert = new AlertDialog.Builder(this);
            correctAlert.setCancelable(false);
            correctAlert.setTitle("Congratulation!");
            correctAlert.setMessage("Your answer is correct!");
            correctAlert.setPositiveButton("Continue", null);
            correctAlert.show();
            mUserScore++;
        }
        else{
            AlertDialog.Builder correctAlert = new AlertDialog.Builder(this);
            correctAlert.setCancelable(false);
            correctAlert.setTitle("I am sorry...!");
            correctAlert.setMessage("Your answer is wrong");
            correctAlert.setPositiveButton("Continue", null);
            correctAlert.show();
        }
//            Toast.makeText(getApplicationContext(),R.string.incorrect_toast_message,Toast.LENGTH_SHORT).show();
    }


    //Lifecycle method

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(getApplicationContext(),"OnStart methods is called", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getApplicationContext(),"OnResume methods is called", Toast.LENGTH_SHORT).show();
//    }
//    //can see, cannot interact
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(getApplicationContext(),"OnPause methods is called", Toast.LENGTH_SHORT).show();
//    }
//    //background
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(),"OnStop methods is called", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(),"OnDestroy methods is called", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_KEY, mUserScore);//use constant on key
        outState.putInt(INDEX_KEY, mQuestionIndex);
    }
}
