package com.dragon.mathias.learnapp.classes;

public class Lecture {

    private String vocable;
    private String meaning;

    //private String writtenAdditional;
    //private Image image;
    //private String audio;

    public Lecture(String vocable, String meaning) {
        this.vocable = vocable;
        this.meaning = meaning;
    }

    public String getVocable() {
        return vocable;
    }

    public void setVocable(String vocable) {
        this.vocable = vocable;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
