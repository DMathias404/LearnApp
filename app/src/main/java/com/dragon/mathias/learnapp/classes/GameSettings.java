package com.dragon.mathias.learnapp.classes;

public class GameSettings {

    private int basicPoints;
    private double pointsMultiplier;
    private int timeCounter;
    private double answerMult;
    private double timeMult;

    public GameSettings() {

    }

    public GameSettings(int basicPoints, double pointsMultiplier, int timeCounter, double answerMult, double timeMult) {
        this.basicPoints = basicPoints;
        this.pointsMultiplier = pointsMultiplier;
        this.timeCounter = timeCounter;
        this.answerMult = answerMult;
        this.timeMult = timeMult;
    }

    public void defaultSettings(){
        this.basicPoints = 300;
        this.pointsMultiplier = 1.1f;
        this.timeCounter = 10;
        this.answerMult = 1;
        this.timeMult = 1;
    }

    public int getBasicPoints() {
        return basicPoints;
    }

    public void setBasicPoints(int basicPoints) {
        this.basicPoints = basicPoints;
    }

    public double getPointsMultiplier() {
        return pointsMultiplier;
    }

    public void setPointsMultiplier(double pointsMultiplier) {
        this.pointsMultiplier = pointsMultiplier;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(int timeCounter) {
        this.timeCounter = timeCounter;
    }

    public double getAnswerMult() {
        return answerMult;
    }

    public void setAnswerMult(double answerMult) {
        this.answerMult = answerMult;
    }

    public double getTimeMult() {
        return timeMult;
    }

    public void setTimeMult(double timeMult) {
        this.timeMult = timeMult;
    }
}
