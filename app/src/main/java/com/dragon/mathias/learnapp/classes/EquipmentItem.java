package com.dragon.mathias.learnapp.classes;

public class EquipmentItem {
    private String name;
    private String description;
    private GameSettings effects;

    public EquipmentItem(String name, String description, GameSettings effects) {
        this.name = name;
        this.description = description;
        this.effects = effects;
    }

    public String toString(){
        return this.name;
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

    public GameSettings getEffects() {
        return effects;
    }

    public void setEffects(GameSettings effects) {
        this.effects = effects;
    }
}
