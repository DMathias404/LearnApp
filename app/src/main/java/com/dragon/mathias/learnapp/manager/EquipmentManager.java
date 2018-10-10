package com.dragon.mathias.learnapp.manager;

import android.content.Context;
import android.util.Log;

import com.dragon.mathias.learnapp.classes.Character;
import com.dragon.mathias.learnapp.classes.EquipmentItem;
import com.dragon.mathias.learnapp.classes.Quest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

//manager-class for loading game-settings
public class EquipmentManager {

    Context context;

    //constructor for creating instance with current application-context
    public EquipmentManager(Context _context){
        this.context = _context;
    }

    //return arraylist with all loaded settings
    public ArrayList<EquipmentItem> getEquipmentList(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(loadEquipmentlistFromAsset(), new TypeToken<ArrayList<EquipmentItem>>(){}.getType());
    }

    //load all settings from json-file
    public String loadEquipmentlistFromAsset() {
        String json;
        try {
            //input-stream from json-file
            InputStream is = context.getApplicationContext().getAssets().open("equipment.json");
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
