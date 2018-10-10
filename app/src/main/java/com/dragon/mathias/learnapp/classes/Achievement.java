package com.dragon.mathias.learnapp.classes;

public class Achievement {

    private String name;
    private String description;
    private int exp;
    private int coin;
    private boolean completed;

    public Achievement(String name, int exp) {
        this.name = name;
        this.exp = exp;
        this.completed = false;
    }

    public Achievement(String name, String description, int exp) {
        this.name = name;
        this.description = description;
        this.exp = exp;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
