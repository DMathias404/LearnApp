package com.dragon.mathias.learnapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragon.mathias.learnapp.classes.Character;
import com.dragon.mathias.learnapp.classes.Quest;
import com.dragon.mathias.learnapp.manager.CharacterManager;

import java.util.ArrayList;

//activity for game-over-screen
public class GameOverActivity extends AppCompatActivity {

    //values
    private long score;
    private long hiScore;
    int exp, req, expCounter, expGain;

    //layout
    private String answerStats;
    private TextView textExpGain;
    private TextView textLevel;
    private ProgressBar progressLevel;

    //manager
    private Character character;
    private CharacterManager cManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        cManager = new CharacterManager(getApplicationContext());

        //retrieve intent values
        Intent intent = getIntent();
        score = intent.getLongExtra("score", 0);
        hiScore = intent.getLongExtra("hiScore", 0);
        answerStats = intent.getStringExtra("correctAnswers");
        Log.i("score", "Score: " + score);

        //prepare layout fields
        character = cManager.loadCharacterData();
        progressLevel = findViewById(R.id.progressLevel);
        textExpGain = findViewById(R.id.textExpGain);
        textLevel = findViewById(R.id.textLevel);
        textLevel.setText(String.format(getString(R.string.level), character.getLevel()));

        //calculate experience points
        addToGainExp(calcExpFromScore());
        concludeQuests();
        gainExp(this.expGain);

        //save new character-data
        cManager.saveCharacterData(character);

        TextView textScore = findViewById(R.id.textScore);
        textScore.setText(String.valueOf(score));

        //check for new highscore
        if (score > hiScore) {
            TextView scoreAlert = findViewById(R.id.textViewScoreAlert);
            scoreAlert.setText(getString(R.string.newHiScore));
            scoreAlert.setTextColor(Color.parseColor("#6e9135"));
        }

        //layout
        TextView textAnswersStat = findViewById(R.id.textAnswersStat);
        String stat = intent.getStringExtra("correctAnswers");
        textAnswersStat.setText(String.format(getString(R.string.answerScore), stat));

        Button startButton = findViewById(R.id.buttonBack);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoLessons = new Intent(GameOverActivity.this, SelectionActivity.class);
                gotoLessons.putExtra("score", score);
                startActivity(gotoLessons);
            }
        });
    }

    //conclude all character quests. Both, completion and progress
    private void concludeQuests(){
        ArrayList<Quest> itemsToRemove = new ArrayList<>();

        //check every quest-progress
        for(Quest q : character.getQuests()){
            q.checkThisQuest(score, hiScore, character, answerStats);

            //checkfor completion
            if(q.isCompleted()){
                itemsToRemove.add(q);
                addToGainExp(q.getReward());
                Log.i("Debug", "Quest completed");
            }
            //open quest dialog
            showQuestDialog(this, q);
        }

        //remove completed quests from character
        for(Quest q : itemsToRemove){
            character.getQuests().remove(q);
        }
    }

    //open quest dialog
    private void showQuestDialog(Context context, Quest quest) {
        Log.i("Debug", "Open Quest Dialog");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quest);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        TextView textTitle = dialog.findViewById(R.id.dialogTextTitle);
        textTitle.setText(quest.getName());
        TextView textDescription = dialog.findViewById(R.id.dialogTextDescription);
        textDescription.setText(quest.getDescription());
        TextView textProgress = dialog.findViewById(R.id.dialogTextProgress);
        textProgress.setText(String.format(getString(R.string.progress), quest.getProcess(), quest.getRequirement()));

        TextView textReward = dialog.findViewById(R.id.dialogTextReward);
        TextView textEventType = dialog.findViewById(R.id.dialogTextEventType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar xpBar = findViewById(R.id.progressLevel);
            xpBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorXPbar)));
        }

        //differentiating between completed and non-completed quests
        if(quest.isCompleted()) {
            textEventType.setText(getString(R.string.questComplete));
            textEventType.setTextColor(Color.GREEN);
            textReward.setText(String.format(getString(R.string.gainExp), quest.getReward()));
        }else {
            textEventType.setText("");
            textReward.setText(String.format(getString(R.string.reward), quest.getReward()));
        }

        Button btnOk = dialog.findViewById(R.id.dialogButtonOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }

    //calculate experience-gain from play-score
    private int calcExpFromScore(){
        int gain = (int) score / 300;
        gain = gain * 10;

        return gain;
    }

    //add exp-points to gain
    private void addToGainExp(int add){
        this.expGain += add;
    }

    //add experience to character
    private void gainExp(int gain) {

        exp = character.getExperience();
        req = character.getRequiredExp();

        progressLevel.setMax(req);
        progressLevel.setProgress(exp);

        textExpGain.setText(String.format(getString(R.string.gainExp), gain));

        final int newExp = exp + gain;

        Log.d("Exp-Debug", "Gained Exp: " + gain);
        Log.d("Exp-Debug", "Exp-ProgressLevel: " + progressLevel.getProgress());
        Log.d("Exp-Debug", "New Exp-Progress: " + newExp);


        //processing exp gain and level-up
                if (newExp >= req) {
                    int newLevel = character.getLevel() + 1;
                    character.setLevel(newLevel);
                    textLevel.setText(String.format(getString(R.string.level), newLevel));
                    int newReq = (int) (character.getRequiredExp() * 1.15);
                    newReq = (newReq / 10);
                    newReq = newReq * 10;
                    character.setRequiredExp(newReq);
                    character.setExperience(newExp - req);
                } else {
                    character.setExperience(newExp);
                }

        //Animation of ProgressBar
        new Thread(new Runnable() {
            public void run() {
                expCounter = 0;
                if (newExp >= req) {
                    expCounter = exp;
                    while (expCounter < req) {
                        expCounter++;
                        // Update the progress bar
                        handler.post(new Runnable() {
                            public void run() {
                                progressLevel.setProgress(expCounter);
                            }
                        });
                        try {
                            // Sleep
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    expCounter = 0;
                    while (expCounter < (newExp - req)) {
                        expCounter++;
                        // Update the progress bar
                        handler.post(new Runnable() {
                            public void run() {
                                progressLevel.setProgress(expCounter);
                            }
                        });
                        try {
                            // Sleep
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    expCounter = exp;
                    while (expCounter < newExp) {
                        expCounter++;
                        // Update the progress bar
                        handler.post(new Runnable() {
                            public void run() {
                                progressLevel.setProgress(expCounter);
                            }
                        });
                        try {
                            // Sleep
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
