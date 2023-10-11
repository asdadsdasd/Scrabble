package model.events;

import model.entity.Letter;
import model.entity.Player;

import java.awt.event.ActionEvent;
import java.util.EventObject;

public class PlayerActionEvent extends EventObject {
    // -------------------------------- Игрок --------------------------------------
    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player player() {
        return player;
    }

    // -------------------------------- Активная буква --------------------------------------
    Letter letter;

    public void setLetter(Letter letter) {
        this.letter = letter;
    }

    public Letter letter() {
        return letter;
    }

    String currentWord;

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String currentWord() {
        return currentWord;
    }

    public PlayerActionEvent(Object source) {
        super(source);
    }
}
