package com.dragon.mathias.learnapp.manager;

import android.content.Context;
import android.util.Log;

import com.dragon.mathias.learnapp.classes.Character;
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

public class QuestManager {

    Context context;

    //constructor for instancing manager with current application-context
    public QuestManager(Context _context){
        this.context = _context;
    }

    //add a new quest to character
    public void addQuest(Character character) {
        ArrayList<Quest> qlist = getQuestList();
        ArrayList<Quest> toRemove = new ArrayList<Quest>();

        Log.i("quest", "List1: " + qlist.size());
        //add current quests to a list to be removed from possible additions
        for(Quest q1 : character.getQuests()){
            for(Quest q2 : qlist){
                if(q1.equals(q2)){
                    toRemove.add(q2);
                    Log.i("quest", "removed");
                }
            }
        }

        //remove current quests from possible quests
        for(Quest q : toRemove){
            qlist.remove(q);
        }
        Log.i("quest", "List2: " + qlist.size());

        Quest q = qlist.get(new Random().nextInt(qlist.size()));
        character.getQuests().add(q);
    }

    //check the time, when last quest was added
    //add new quest, if last time was more than a day ago
    public void checkQuestUpdate(Character character) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(new Date());
        cal2.setTime(character.getLastQuest());
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        //max. 3 quests in log available
        if(!sameDay && character.getQuests().size() < 3){
            addQuest(character);
            character.setLastQuest(new Date());
        }
    }

    //return a list of all possible quests from list
    public ArrayList<Quest> getQuestList(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(loadQuestlistFromAsset(), new TypeToken<ArrayList<Quest>>(){}.getType());
    }

    //load quests from json-file
    public String loadQuestlistFromAsset() {
        String json;
        try {
            InputStream is = context.getApplicationContext().getAssets().open("quests.json");
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
