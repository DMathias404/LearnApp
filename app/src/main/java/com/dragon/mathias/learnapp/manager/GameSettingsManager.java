package com.dragon.mathias.learnapp.manager;

import android.content.Context;
import android.util.Log;

import com.dragon.mathias.learnapp.classes.GameSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//unused manager-class
//>>obsolete<<
public class GameSettingsManager {

    Context context;

    //constructor for instancing manager with current application-context
    public GameSettingsManager(Context _context){
        this.context = _context;
    }

    public GameSettings loadGameSettings() {
        GameSettings settings;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = null;

        FileInputStream fis = null;
        File file = context.getFileStreamPath("gameSettings");
        if (file.exists()) {
            Log.i("Debug", "gameSettings was found");
            try {
                fis = context.openFileInput("gameSettings");

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

            settings = gson.fromJson(json, GameSettings.class);
        }else{
            settings = new GameSettings();
            settings.defaultSettings();
        }

        return settings;
    }

    public void saveGameData(GameSettings settings) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String filename = "gameSettings";
        String fileContents = gson.toJson(settings);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
