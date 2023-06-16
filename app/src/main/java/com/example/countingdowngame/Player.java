package com.example.countingdowngame;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

class Player implements Serializable {
    private final String photo;
    private String name;
    private Game game;
    private final Set<Settings_WildCard_Probabilities> usedWildCards = new HashSet<>();
    private int wildCardAmount = 8;
    private boolean selected;


    public void setWildCardAmount(int wildCardAmount) {
        this.wildCardAmount = wildCardAmount; // Access the static variable using the class name
    }

    public Player(String photo, String name) {
        this.photo = photo;
        this.name = name;
        this.selected = false;
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


    public int getWildcardPerPlayer() {
        return wildCardAmount;
    }

    public void useWildCard() {
        if (game != null) {
            game.triggerPlayerEvent(new PlayerEvent(this, PlayerEventType.WILD_CARD));
        }
        wildCardAmount--;
    }

    public void useSkip() {
        this.game.triggerPlayerEvent(new PlayerEvent(this, PlayerEventType.SKIP));
    }

    public void addUsedWildCard(Settings_WildCard_Probabilities usedWildCard) {
        usedWildCards.add(usedWildCard);
    }

    public void resetAbilities(int wildcardAmount) {
        this.wildCardAmount = wildcardAmount;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}