package com.example.countingdowngame.createPlayer;

public class CharacterClassStore {
    private final String className;
    private final String specialAbility;
    private final int id; // Unique identifier for each item
    private boolean isSelected; // Variable to track the selection state

    public CharacterClassStore(int id, String className, String specialAbility) {
        this.id = id;
        this.className = className;
        this.specialAbility = specialAbility;
        this.isSelected = false; // Initialize isSelected to false by default
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getCharacterClassDescriptions() {
        return specialAbility;
    }

    public boolean isSelected() {
        return isSelected; // Return the selection state
    }

    public void setSelected(boolean selected) {
        isSelected = selected; // Set the selection state
    }

    public boolean getSelected() {
        return isSelected;
    }
}
