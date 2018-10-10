package com.dragon.mathias.learnapp.classes;

public class Quest {
    private int id;
    private String name;
    private String description;
    private int type;
    private int requirement;
    private long process;
    private int reward;
    private boolean completed;

    public void checkThisQuest(long score, long hiScore, Character character, String statsString) {

        //types
        //0 - earn X points in one game
        //1 - answer X percent correctly
        //2 - gather X points
        //3 - play mode (quiz) X times
        //4 - play a lecture, which is already completed

        switch(this.type){
            case 0: this.process = score;
                break;
            case 1: String [] val = statsString.split("/");
                    int stat = Integer.parseInt(val[0])/Integer.parseInt(val[1]);
                    this.process = stat*100;
                break;
            case 2: this.process += score;
                break;
            case 3: this.process++;
                break;
            case 4:  if(hiScore>0)
                     this.process = requirement;
                break;

        }

        if(process>=requirement)
            completed = true;
    }

    public Quest(int id, String name, int type, int requirement, int reward) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.process = 0;
        this.requirement = requirement;
        this.reward = reward;
        this.completed = false;
    }

    public boolean equals(Quest q2) {
        if (!(q2 instanceof Quest)) {
            return false;
        }

        // Custom equality check here.
        return this.getId() == q2.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public long getProcess() {
        return process;
    }

    public void setProcess(long process) {
        this.process = process;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}