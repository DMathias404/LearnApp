package com.dragon.mathias.learnapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragon.mathias.learnapp.classes.GameSettings;
import com.dragon.mathias.learnapp.classes.Lecture;
import com.dragon.mathias.learnapp.classes.Lesson;
import com.dragon.mathias.learnapp.classes.LevelData;
import com.dragon.mathias.learnapp.manager.CharacterManager;
import com.dragon.mathias.learnapp.manager.GameDataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//Activity for quiz gamemode with reversed vocales
public class QuizReverseActivity extends AppCompatActivity {

    //UI
    private LinearLayout linearLayout;

    //data & lists
    private Lesson currentLesson;
    private String currentLessonFile;
    private ArrayList<Lecture> lectures;
    private ArrayList<Lecture> questionPool;
    private ArrayList<Button> buttonList;
    private ArrayList<LevelData> gameData;

    //question logic
    private int questionCounter;
    private boolean startup = true;
    private boolean answerPossible = false;
    private int correctAnswerCounter = 0;

    private GameSettings settings;

    private double points;
    private ProgressBar timer;
    private long score = 0;
    private Boolean countdownRunning;
    private long questionTime;

    private String gamemode;

    GameDataManager dataManager;
    CharacterManager characterManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dataManager  = new GameDataManager(getApplicationContext());
        characterManager = new CharacterManager(getApplicationContext());

        if (startup) {
            init();
            startup = false;
            dataManager.listAssetFiles();
        }
    }

    //initializing
    public void init() {
        Log.i("Debug", "Startup");

        //layout
        linearLayout = findViewById(R.id.linearLayout);

        //data
        Intent intent = getIntent();
        currentLessonFile = intent.getStringExtra("selection");

        //get from intent
        gamemode = intent.getStringExtra("gamemode");

        currentLesson = dataManager.getLesson(currentLessonFile);
        lectures = currentLesson.getLectureList();
        questionPool = new ArrayList<>();

        gameData = dataManager.loadGameData(gamemode);
        settings = characterManager.loadCharacterData().getEquipment().getEffects();

        //points
        points = settings.getBasicPoints() * settings.getPointsMultiplier();

        //questions
        questionCounter = 0;
        startQuestion(questionCounter);
    }

    //show a question
    public void startQuestion(int index) {
        //increasing size of question-pool
        if (questionPool.size() < lectures.size()) {
            increasePool();
        }
        final Lecture lecture = questionPool.get(index);
        buttonList = new ArrayList<>();
        linearLayout.removeAllViews();
        Log.i("Question", lecture.getMeaning());

        //timer logic
        timer = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
        linearLayout.addView(timer);
        questionTime = System.currentTimeMillis();

        timer.setMax(settings.getTimeCounter());
        timer.setProgress(settings.getTimeCounter());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) timer.getLayoutParams();
        params.setMargins(16, 8, 16, 0);
        timer.setLayoutParams(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            timer.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
        countdownRunning = true;

        new Thread(new Runnable() {
            public void run() {
                int c = settings.getTimeCounter();
                while (c > 0 && countdownRunning) {
                    c--;
                    // Update the progress bar and display the
                    //current value in the text view
                    final int finalC = c;
                    handler.post(new Runnable() {
                        public void run() {
                            timer.setProgress(finalC);
                            if(finalC <= (0.4 * settings.getTimeCounter())){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    timer.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                                }

                            }
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        TextView questionText = new TextView(this);
        questionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        questionText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        questionText.setText(lecture.getMeaning());
        linearLayout.addView(questionText);

        //block input after answering a question
        answerPossible = true;

        //create answer-buttons
        for (final Lecture l : getChoices(lecture)) {
            final Button b = new Button(this);
            b.setText(l.getVocable());
            b.setBackgroundColor(getResources().getColor(R.color.answerButtonDefault));
            b.setLayoutParams(params);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (answerPossible) {
                        checkAnswer(lecture, l, b);
                        answerPossible = false;
                    }
                }
            });
            linearLayout.addView(b);
            buttonList.add(b);
        }

        questionTime = System.currentTimeMillis();

    }

    //checking answer
    public void checkAnswer(Lecture l1, Lecture l2, Button b) {
        countdownRunning = false;
        double answerMult = settings.getAnswerMult();
        double timeMult = settings.getTimeMult();
        if (l1.equals(l2)) {
            //correct
            correctAnswerCounter++;
            Log.i("Answer", "correct");
            b.setBackgroundColor(getResources().getColor(R.color.answer_correct));
        } else {
            //false
            answerMult = -0.7;
            timeMult = 0;
            Log.i("Answer", "false");
            b.setBackgroundColor(getResources().getColor(R.color.answer_wrong));
            for (Button but : buttonList) {
                if (l1.getVocable().equals(but.getText().toString())) {
                    but.setBackgroundColor(getResources().getColor(R.color.answer_correct));
                    break;
                }
            }
        }

        //time needed for answering the question + calculating time bonus points
        long timeDifference = System.currentTimeMillis() - questionTime;
        long timePoints = (settings.getTimeCounter() * 1000) - timeDifference;
        if(timePoints < 0){
            timePoints = 0;
        }else {
            timePoints = (int) timePoints/70;
        }

        //adding up all points
        score += (points * answerMult);
        score += (timePoints * timeMult);
        if (score < 0) {
            score = 0;
        }

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                questionCounter++;
                if (questionCounter < lectures.size()) {
                    startQuestion(questionCounter);
                } else {
                    onGameOver();
                }
            }
        }.start();
    }

    //create answers to a question
    //correct answer + 3 random answers to previous questions
    public ArrayList<Lecture> getChoices(Lecture lecture) {
        ArrayList<Lecture> pool = new ArrayList<>();
        pool.addAll(questionPool);
        pool.remove(lecture);

        ArrayList<Lecture> choices = new ArrayList<>();
        Random rnd = new Random();

        //adding correct answer
        choices.add(lecture);

        //fills up choices
        //up to 3 or the number of previous questions
        for (int i = 0; i < 3 && pool.size() > 0; i++) {
            int choice = rnd.nextInt(pool.size());
            choices.add(pool.get(choice));
            pool.remove(choice);
        }

        //shuffle order
        Collections.shuffle(choices);

        return choices;
    }

    //finishing up the quiz
    //is called after last question is answered
    public void onGameOver() {
        Log.i("score", "Score: " + score);

        Intent theNextIntent = new Intent(getApplicationContext(), GameOverActivity.class);
        theNextIntent.putExtra("score", score);
        theNextIntent.putExtra("correctAnswers", correctAnswerCounter + "/" + questionCounter);

        //saving highscore for level
        for(LevelData ld : gameData){
            if(ld.getLevelName().equals(currentLessonFile)){
                theNextIntent.putExtra("hiScore", ld.getHiScore());
                if(ld.getHiScore() < score)
                ld.setHiScore(score);
            }
        }
        //save data to file
        dataManager.saveGameData(gameData, gamemode);

        //go to gameover-activity
        startActivity(theNextIntent);
        this.finish();
    }

    //add question to question-pool
    public void increasePool() {
        if (!questionPool.contains(lectures.get(questionPool.size())) && questionPool.size() < lectures.size()) {
            questionPool.add(lectures.get(questionPool.size()));
        }
    }
}
