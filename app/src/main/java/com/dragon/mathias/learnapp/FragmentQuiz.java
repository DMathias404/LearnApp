package com.dragon.mathias.learnapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

//Fragment for SelectionActivity
public class FragmentQuiz extends Fragment {

    public FragmentQuiz() {
        // Required empty public constructor
    }

    private ArrayList<LevelData> gameData;
    private ArrayList<String> lessonList;
    private GameDataManager dataManager;

    //set gamemode
    String gamemode = "Quiz";

    GridView lessonView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        lessonView = view.findViewById(R.id.lessonQuizView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataManager = new GameDataManager(getActivity().getApplicationContext());
        init();
    }

    //initializing data and layout
    private void init() {

        lessonView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = lessonView.getItemAtPosition(i);

                Intent gotoLecture = new Intent(getActivity(), QuizActivity.class);
                gotoLecture.putExtra("selection", o.toString());
                gotoLecture.putExtra("gamemode", gamemode);
                startActivity(gotoLecture);
            }
        });
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        gameData = dataManager.loadGameData(gamemode);
        lessonList = dataManager.listAssetFiles();
        final ArrayList<Lesson> lessons = new ArrayList<>();
        for (String s : lessonList) {
            String json = dataManager.loadJSONFromAsset(s);
            lessons.add(gson.fromJson(json, Lesson.class));
        }

        dataManager.checkGameData(gamemode);

        //custom adapter for gridview
        lessonView.setAdapter(new ArrayAdapter<String>(
                getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, lessonList) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                Context context = this.getContext();

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View gridView;

                if (convertView == null) {

                    gridView = new View(context);

                    // get layout from xml
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
                    pointsTextView.setTextColor(getResources().getColor(R.color.colorText));

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(getResources().getColor(R.color.colorAccent)); // Changes this drawbale to use a single color instead of a gradient
                    //gd.setCornerRadius(5);
                    gd.setStroke(3, Color.BLACK);
                    for(LevelData ld : gameData){
                        if(ld.getLevelName().equals(str)){
                            pointsTextView.setText(String.valueOf(ld.getHiScore()));
                            if(ld.getHiScore() > 0){
                                gd.setStroke(5, getResources().getColor(R.color.borderColor));
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
}
