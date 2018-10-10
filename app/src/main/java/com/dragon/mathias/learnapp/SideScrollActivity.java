package com.dragon.mathias.learnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dragon.mathias.learnapp.classes.Lecture;
import com.dragon.mathias.learnapp.classes.Lesson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//unused
public class SideScrollActivity extends AppCompatActivity {

    public ArrayList<Lecture> vocablePool = new ArrayList<Lecture>();
    public Lesson currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_lecture);
        setContentView(new GameView(this));
    }

    public void onGameOver() {
        //Intent theNextIntent = new Intent(getApplicationContext(), GameOverActivity.class);
        Intent theNextIntent = new Intent(getApplicationContext(), LessonActivity.class);
        startActivity(theNextIntent);
        this.finish();
    }

    public Lesson getLesson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(loadJSONFromAsset(), Lesson.class);
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = this.getApplicationContext().getAssets().open("lessons/021_colors.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
