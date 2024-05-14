package com.example.countingdowngame.game;

import android.content.Context;

import com.example.countingdowngame.settings.GeneralSettingsLocalStore;

import java.io.Serializable;

public class Player implements Serializable {

    //-----------------------------------------------------Initialize---------------------------------------------------//

    private final String photo;
    private String name;
    private String classChoice;
    private Game game;
    private int wildCardAmount;
    private boolean selected;
    private final int turnCounter;
    private boolean usedClassAbility;
    private boolean justUsedClassAbility;

    private boolean usedWildCard;
    private boolean removed;
    private int specificActiveTurnCounter; // Add a counter for the active turns of the Survivor class


    //-----------------------------------------------------Set Game---------------------------------------------------//

    public void setGame(Game game) {
        this.game = game;
    }

    //-----------------------------------------------------Player---------------------------------------------------//
    public Player(Context context, String photo, String name, String classChoice) {
        this.photo = photo;
        this.name = name;
        this.classChoice = classChoice;
        this.selected = false;
        this.usedClassAbility = false;
        this.justUsedClassAbility = false;
        this.usedWildCard = false;
        this.removed = false;
        resetWildCardAmount(context);
        this.turnCounter = 0; // Initialize the turn counter to 0
        this.specificActiveTurnCounter = 0; // Initialize the Survivor class active turn counter to 0
    }

    //-----------------------------------------------------Survivor Ability---------------------------------------------------//

    public void incrementSpecificTurnCounter() {
        specificActiveTurnCounter++;
    }
    public void resetSpecificTurnCounter() {
        specificActiveTurnCounter = 0;
    }
    public int getSpecificActiveTurnCounter() {
        return specificActiveTurnCounter;
    }

    //--------------------------------------------------------------------------------------------------------//

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean getUsedClassAbility() {
        return usedClassAbility;
    }

    public void setUsedClassAbility(boolean classAbility) {
        this.usedClassAbility = classAbility;
    }

    public void setJustUsedClassAbility(boolean justUsedClassAbility) {
        this.justUsedClassAbility = justUsedClassAbility;
    }

    public boolean getJustUsedClassAbility() {
        return justUsedClassAbility;
    }

    public void setJustUsedWildCard(boolean used) {
        this.usedWildCard = used;
    }

    public boolean getJustUsedWildCard() {
        return this.usedWildCard;
    }


    public void setInRepeatingTurn() {
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public String getClassChoice() {
        return classChoice;
    }

    public void setClassChoice(String classChoice) {
        this.classChoice = classChoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //-----------------------------------------------------Wild Card/Skip---------------------------------------------------//

    public int getWildCardAmount() {
        return wildCardAmount;
    }
    public void useWildCard() {
        if (game != null) {
            game.triggerPlayerEvent(new PlayerEvent(this, PlayerEventType.WILD_CARD));
        }
        wildCardAmount--; // Decrease the wildcard amount
    }
    public void useSkip() {
        this.game.triggerPlayerEvent(new PlayerEvent(this, PlayerEventType.SKIP));
    }

    //-----------------------------------------------------Reset Abilities---------------------------------------------------//

    public void resetWildCardAmount(Context context) {
        wildCardAmount = GeneralSettingsLocalStore.fromContext(context).playerWildCardCount();
    }
    public void gainWildCards(int numberOfCardsToGain) {
        wildCardAmount += numberOfCardsToGain;
    }
    public void loseWildCards(int numberOfWildCardsToLose) {
        wildCardAmount = Math.max(wildCardAmount - numberOfWildCardsToLose, 0);
    }


}