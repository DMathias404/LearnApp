package com.dragon.mathias.learnapp.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dragon.mathias.learnapp.classes.Lesson;
import com.dragon.mathias.learnapp.classes.LevelData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

//manager-class for loading and saving gamemode-Data
public class GameDataManager {

    Context context;

    //constructor for instancing manager with current application-context
    public GameDataManager(Context _context){
        this.context = _context;
    }

    //loaging gamemode-data (for specific gamemode) from file
    public ArrayList<LevelData> loadGameData(String gamemode) {
        ArrayList<String> levelNames = new ArrayList<String>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = null;

        ArrayList<LevelData> gameData = null;
        FileInputStream fis = null;
        String filename = "gameData" + gamemode;
        File file = context.getFileStreamPath(filename);
        if (file.exists()) {
            Log.i("Debug", "gameData was found");
            try {
                fis = context.openFileInput("gameData" + gamemode);

                //read data from file
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //converting json-string to leveldata-arraylist
            gameData = gson.fromJson(json, new TypeToken<ArrayList<LevelData>>() {
            }.getType());
        }

        if(gameData == null){
            gameData = initializeGamedata();
        }

        return gameData;
    }

    //checking gamemode-data for it's contents; may creates new data-files, if data is empty/not existing
    //loading and saving gamemode-data
    public void checkGameData(String gamemode) {
        ArrayList<String> levelNames = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = null;
        ArrayList<LevelData> gameData = null;
        ArrayList<String> lessonList = listAssetFiles();

        FileInputStream fis;
        String filename = "gameData" + gamemode;
        File file = context.getFileStreamPath(filename);
        if (file.exists()) {
            Log.i("Debug", "gameData was found");
            try {
                fis = context.openFileInput(filename);

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
        }else {
            gameData = initializeGamedata();
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

        String fileContents = gson.toJson(gameData);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //creates new gamedata for each level
    private ArrayList<LevelData> initializeGamedata(){
        ArrayList<LevelData> gamedata = new ArrayList<LevelData>();
        for(String s : listAssetFiles()){
            gamedata.add(new LevelData(s));
        }

        return gamedata;
    }

    //saving gamemode-data to file
    public void saveGameData(ArrayList<LevelData> gameData, String gamemode) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String filename = "gameData" + gamemode;
        //converting gamedata-object to json
        String fileContents = gson.toJson(gameData);
        FileOutputStream outputStream;

        try {
            //write data to file
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loading lessons from all json-files in the assets/lessons/-folder
    public String loadJSONFromAsset(String file) {
        String json;
        try {
            InputStream is = context.getAssets().open("lessons/" + file);
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

    //return arraylist with all names of the lessons
    public ArrayList<String> listAssetFiles() {
        String[] list;
        ArrayList<String> lessons = new ArrayList<String>();
        try {
            list = context.getAssets().list("lessons");
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    lessons.add(file);
                }
            }
        } catch (IOException e) {
        }

        return lessons;
    }

    //get currently selected lesson
    public Lesson getLesson(String currentLessonFile) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(loadJSONFromAsset(currentLessonFile), Lesson.class);
    }
}
