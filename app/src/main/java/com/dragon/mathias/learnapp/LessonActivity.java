package com.dragon.mathias.learnapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.mathias.learnapp.classes.Lesson;
import com.dragon.mathias.learnapp.classes.LevelData;
import com.dragon.mathias.learnapp.manager.GameDataManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//template for fragments
public class LessonActivity extends AppCompatActivity {

    private ArrayList<LevelData> gameData;
    private ArrayList<String> lessonList;
    private GameDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        dataManager = new GameDataManager(getApplicationContext());
        init();
    }

    private void init() {
        final GridView lessonView = findViewById(R.id.lessonView);
        lessonView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = lessonView.getItemAtPosition(i);

                Intent gotoLecture = new Intent(LessonActivity.this, QuizActivity.class);
                gotoLecture.putExtra("selection", o.toString());
                startActivity(gotoLecture);
            }
        });
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        gameData = new ArrayList<>();
        lessonList = dataManager.listAssetFiles();
        final ArrayList<Lesson> lessons = new ArrayList<>();
        for (String s : lessonList) {
            String json = dataManager.loadJSONFromAsset(s);
            lessons.add(gson.fromJson(json, Lesson.class));
        }

        checkGameData();

        lessonView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, lessonList) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                Context context = this.getContext();

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View gridView;

                if (convertView == null) {

                    gridView = new View(context);

                    // get layout from mobile.xml
                    gridView = inflater.inflate(R.layout.lesson_grid_cell, null);

                    // set value into textview
                    TextView nameTextView = (TextView) gridView
                            .findViewById(R.id.grid_item_name);

                    // Set the layout parameters for TextView widget
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    nameTextView.setLayoutParams(lp);

                    // Get the TextView LayoutParams
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nameTextView.getLayoutParams();

                    // Set the TextView layout parameters
                    nameTextView.setLayoutParams(params);

                    // Display TextView text in center position
                    nameTextView.setGravity(Gravity.CENTER);

                    // Set the TextView text font family and text size
                    nameTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                    nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    final float scale = getResources().getDisplayMetrics().density;
                    gridView.setPadding(0, (int) scale * 10, 0, (int) scale * 10);

                    String str = lessonList.get(position);
                    nameTextView.setText(lessons.get(position).getName());
                    nameTextView.setTag(str);


                    TextView pointsTextView = gridView
                            .findViewById(R.id.grid_item_points);

                    pointsTextView.setLayoutParams(lp);
                    LinearLayout.LayoutParams params_p = (LinearLayout.LayoutParams) pointsTextView.getLayoutParams();
                    pointsTextView.setLayoutParams(params_p);
                    pointsTextView.setGravity(Gravity.CENTER);
                    pointsTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                    pointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    pointsTextView.setText("");
                    pointsTextView.setTextColor(Color.parseColor("#24539e"));

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.parseColor("#989966")); // Changes this drawbale to use a single color instead of a gradient
                    //gd.setCornerRadius(5);
                    gd.setStroke(3, 0xFF000000);
                    for(LevelData ld : gameData){
                        if(ld.getLevelName().equals(str)){
                            pointsTextView.setText(String.valueOf(ld.getHiScore()));
                            if(ld.getHiScore() > 0){
                                gd.setStroke(3, 0xFF98f442);
                            }
                        }
                    }
                    gridView.setBackground(gd);

                } else {
                    gridView = convertView;
                }

                return gridView;
            }
        });
    }

    private void checkGameData() {
        ArrayList<String> levelNames = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = null;

        Context context = getApplicationContext();
        FileInputStream fis;
        File file = context.getFileStreamPath("gameData");
        if (file.exists()) {
            Log.i("Debug", "gameData was found");
            try {
                fis = context.openFileInput("gameData");

                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            gameData = gson.fromJson(json, new TypeToken<ArrayList<LevelData>>() {
            }.getType());
        }

        if (gameData != null) {
            for (LevelData ld : gameData) {
                levelNames.add(ld.getLevelName());
            }
        }
        for (String s : lessonList) {
            if (!levelNames.contains(s)) {
                gameData.add(new LevelData(s));
            }
        }

        String filename = "gameData";
        String fileContents = gson.toJson(gameData);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
