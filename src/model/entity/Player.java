package model.entity;

import model.events.GameEvent;
import model.events.GameListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;

import java.awt.*;
import java.lang.module.FindException;
import java.util.ArrayList;

public class Player {
    // --------------------------------- Поля связанные с игроком -------------------------------
    private int playerScore = 0;

    private String name;

    public String name(){
        return this.name;
    }

    private String currentWord = "";

    public String currentWord(){
        return currentWord;
    }


    // ----------------------- Устанавливаем связь с полем -----------------------
    GameField field;

    public void setField(GameField field) {
        this.field = field;
    }

    public Player (GameField field, String name, GameModel model) {
        model.addGameListener(new GameObserver());
        this.field = field;
        this.name = name;
    }

    // ---------------------- Счёт игрока -----------------------

    public int score(){
        return playerScore;
    }

    public void increaseScore(int points){
        if(points > 0) {
            playerScore += points;
        }
    }

    // ---------------------- Слова, составленные игроком -----------------------
    private ArrayList<String> words = new ArrayList<>();

    public boolean isWordInList(String word){
        return words.contains(word);
    }

    public void addWordToList(){
        if(currentWord.length() > 0)
            words.add(currentWord);
    }


    // ---------------------- Буква, которую нужно установить -----------------------
    private Letter letter;

    public void setActiveLetter(Letter l) {
        if (l != null) {
            this.letter = l;
            l.setPlayer(this);

            // Генерируем событие
            fireLetterIsReceived(this.letter);
        }
    }

    public Letter activeLetter() {
        return this.letter;
    }

    public void setLetterTo(Point pos){

        if (this.letter == null) throw new IllegalArgumentException("Буква не была задана!");

        this.field.setLetter(pos, this.letter);

        // Генерируем событие
        if(this.field.letter(pos) == this.letter) {
            fireLetterIsPlaced(this.letter);
            //this.letter = null;
        }
    }

    // ---------------------- Процесс игры -----------------------

    public void chooseLetter(Point pos){
        Letter chosenLetter = field.letter(pos);
        if(chosenLetter == null) throw new FindException("В выбранной клетке нет буквы!");

        chosenLetter.setChosen(true);
        currentWord += String.valueOf(chosenLetter.character());

        fireLetterOnFieldIsChosen(chosenLetter);
    }

    public void skipTurn(){
        fireTurnIsSkipped(this.letter);
        currentWord = "";
        this.letter = null;
    }

    public void endTurn(){
        fireTurnIsOver(this.letter, this.currentWord);
        currentWord = "";
        this.letter = null;
    }

    public void cancel(){
        fireCancel(this.letter, this.currentWord);
        currentWord = "";
        this.letter = null;
    }

    // ---------------------- Порождает события -----------------------------

   private ArrayList<PlayerActionListener> listeners = new ArrayList<>();

    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) {
        this.listeners.add(l);
    }

    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) {
        this.listeners.remove(l);
    }

    // Оповещает слушателей о событии
    protected void fireLetterIsPlaced(Letter l) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        for(PlayerActionListener o : listeners){
            o.letterIsPlaced(event);
        }
    }

    protected void fireLetterIsReceived(Letter l) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        for(PlayerActionListener o : listeners){
            o.letterIsReceived(event);
        }
    }

    protected void fireTurnIsSkipped(Letter l) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        for (PlayerActionListener o : listeners){
            o.turnIsSkipped(event);
        }
    }

    protected void fireLetterOnFieldIsChosen(Letter l) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        for (PlayerActionListener o : listeners){
            o.letterOnFieldIsChosen(event);
        }
    }

    protected void fireTurnIsOver(Letter l, String word) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        event.setCurrentWord(word);
        for (PlayerActionListener o : listeners){
            o.turnIsOver(event);
        }
    }

    protected void fireCancel(Letter l, String word) {
        PlayerActionEvent event = new PlayerActionEvent(this);
        event.setPlayer(this);
        event.setLetter(l);
        event.setCurrentWord(word);
        for (PlayerActionListener o : listeners){
            o.cancel(event);
        }
    }

    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e) {
            playerScore = 0;
        }
        @Override
        public void currentLetterIsChosen(GameEvent e) {}
        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {}
        @Override
        public void wordHasBeenComposed(GameEvent e) {}
    }
}
