package com.dragon.mathias.learnapp.classes;

import java.util.ArrayList;
import java.util.Date;

public class Character {

    private String name;
    private int level;
    private int experience;
    private int requiredExp;
    private String rank;
    private ArrayList<Achievement> achievements;
    private EquipmentItem equipment;
    private ArrayList<Quest> quests;
    private Date lastQuest;

    public Character() {
        //this.name = name;
        this.level = 1;
        this.experience = 0;
        this.requiredExp = 1000;
        this.achievements = new ArrayList<Achievement>();
        GameSettings settings = new GameSettings();
        settings.defaultSettings();
        this.equipment = new EquipmentItem("Basic", "Basic item", settings);
        this.quests = new ArrayList<Quest>();

        this.lastQuest = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    public void setRequiredExp(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public EquipmentItem getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentItem equipment) {
        this.equipment = equipment;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    public Date getLastQuest() {
        return lastQuest;
    }

    public void setLastQuest(Date lastQuest) {
        this.lastQuest = lastQuest;
    }
}
