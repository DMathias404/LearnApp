package com.dragon.mathias.learnapp.classes;

public class LevelData {

    private String levelName;
    private long hiScore;
    private double stat1;

    public LevelData(String _levelName){
        this.levelName = _levelName;
        this.hiScore = 0;

        this.stat1 = 0;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public long getHiScore() {
        return hiScore;
    }

    public void setHiScore(long hiScore) {
        this.hiScore = hiScore;
    }

    public double getStat1() {
        return stat1;
    }

    public void setStat1(double stat1) {
        this.stat1 = stat1;
    }
}
