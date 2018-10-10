package com.dragon.mathias.learnapp.classes;

import java.util.ArrayList;

public class Lesson {
    private String name;
    private ArrayList<Lecture> lectureList;


    public Lesson(String _name){
        this.name = _name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Lecture> getLectureList() {
        return lectureList;
    }

    public void setLectureList(ArrayList<Lecture> lectureList) {
        this.lectureList = lectureList;
    }

    @Override
    public String toString() {
        return name;
    }
}
