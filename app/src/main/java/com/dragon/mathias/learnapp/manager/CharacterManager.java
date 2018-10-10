package com.dragon.mathias.learnapp.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dragon.mathias.learnapp.classes.Character;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//manager-class for loading and saving character-data
public class CharacterManager {

    Context context;

    //create instance with current application-context
    public CharacterManager(Context _context){
        this.context = _context;
    }

    //loading json-contents and return corresponding character-data
    public Character loadCharacterData() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = null;

        FileInputStream fis = null;
        File file = context.getFileStreamPath("characterData");

        Character character = null;

        if (file.exists()) {
            Log.i("Debug", "characterData was found");
            try {
                fis = context.openFileInput("characterData");

                //file input
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                //building the json-string
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert json String to character-object
            character = gson.fromJson(json, Character.class);
        }

        //creating new character, if characterData is empty or non-existent
        if(character == null || !file.exists()){
            character = new Character();
            QuestManager qm = new QuestManager(context);
            qm.addQuest(character);
            qm.addQuest(character);
        }

            return character;
    }

    //saving character data to file
    public void saveCharacterData(Character character) {
        String filename = "characterData";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        //create json-string from object
        String fileContents = gson.toJson(character);
        FileOutputStream outputStream;

        Log.d("Debug", "#2 Saving Character-Exp: " + character.getExperience());

        //write data to file
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
