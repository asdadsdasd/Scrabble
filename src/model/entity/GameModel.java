package model.entity;

import model.events.*;
import model.factory.CellFactory;
import model.view.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {

    // -------------------------------- Поле -------------------------------------
    private GameField field = new GameField();

    public GameField field() {
        return this.field;
    }

    private ComplicatedAlphabet alphabet = new Alphabet();

    public ComplicatedAlphabet alphabet() {
        return this.alphabet;
    }

    private Dictionary dictionary;

    // -------------------------------- Игроки -----------------------------------
    private ArrayList<Player> playerList = new ArrayList<>();
    private int activePlayer;

    private int turnSkipCount = 0;
    public Player activePlayer(){
        return playerList.get(activePlayer);
    }

    public GameModel(GamePanel panel){
        //Добавляем слушателя меню
        panel.addMenuListener(new MenuObserver());

        //Размеры поля по умолчанию
        field.setSize(5, 5);
        dictionary = Dictionary.getDictionary();

        //Создание игроков
        Player p;
        PlayerObserver observer = new PlayerObserver();

        p = new Player(field(), "1", this);
        p.addPlayerActionListener(observer);
        playerList.add(p);
        activePlayer = 0;

        p = new Player(field(), "2", this);
        p.addPlayerActionListener(observer);
        playerList.add(p);
    }

    // ---------------------- Порождение обстановки на поле ---------------------

    private CellFactory cellFactory = new CellFactory();

    private void generateField(){

        field().clear();
        for(int row = 0; row < this.field().height(); row++) {
            for(int col = 0; col < this.field().width(); col++) {
                field().setCell(cellFactory.createCell(new Point(col, row)));
            }
        }
    }

    // ----------------------------- Игровой процесс ----------------------------

    public void startGame(){
        //Генерируем поле
        generateField();
        setMainWord();

        //Передаем ход первому игроку
        activePlayer = playerList.size()-1;
        exchangePlayer();
    }

    public void exchangePlayer(){
        activePlayer++;
        if(activePlayer >= playerList.size()) activePlayer = 0;

        field.setLettersNotChosen();

        if(isFieldFilled()){
            fireGameFinished(determineWinner());
        }
    }

    private void setMainWord() {
        String word = dictionary.getRandomWord(field().width());
        for (int i = 0; i < field().width(); i++){
            field().setLetter(new Point(i, field().height()/2), alphabet.createLetter(word.charAt(i)));
        }
    }
    public void setLetterToActivePlayer(char ch){
        activePlayer().setActiveLetter(alphabet.createLetter(ch));
    }

    private boolean isFieldFilled(){
        for (Cell cell : field.cells()){
            if(cell.letter() == null){
                return false;
            }
        }
        return true;
    }

    private boolean hasWordBeenComposed(String word){
        for (Player p : playerList){
            boolean flag = p.isWordInList(word);
            if(flag == true)
                return true;
        }
        return false;
    }

    public Player determineWinner(){
        turnSkipCount = 0;
        if (playerList.get(0).score() > playerList.get(1).score()){
            return playerList.get(0);
        }
        else if (playerList.get(0).score() < playerList.get(1).score()) {
            return playerList.get(1);
        }
        else return null;
    }

    // ------------------------- Реагируем на действия игрока ------------------

    private class PlayerObserver implements PlayerActionListener {

        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            if (e.player() == activePlayer()) {
                fireLetterIsPlaced(e);
            }
        }

        @Override
        public void letterIsReceived(PlayerActionEvent e) {
            if (e.player() == activePlayer()) {
                fireLetterIsReceived(e);
            }
        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            if (e.letter() != null) {
                if (e.letter().cell() != null) {
                    Letter l = e.letter();
                    l.unsetCell();
                    l.unsetPlayer();
                }
            }
            if (e.player() == activePlayer()) {
                fireTurnIsSkipped(e);
            }

            turnSkipCount++;
            field.setLettersNotChosen();

            if (turnSkipCount == playerList.size()) {
                fireGameFinished(determineWinner());
            } else {
                exchangePlayer();
            }
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {
            if (e.player() == activePlayer()) {
                fireLetterOnFieldIsChosen(e);
            }

            if (e.letter() == e.player().activeLetter()) {
                fireCurrentLetterIsChosen(e.player());
            }
        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            if (hasWordBeenComposed(e.player().currentWord())) {
                fireWordHasBeenComposed(activePlayer());
                return;
            }
            if (!Dictionary.isContainsWord(e.player().currentWord())) {
                fireDictionaryHasNotContainsWord(e.player());
                return;
            }

            if (e.player() == activePlayer()) {
                fireTurnIsOver(e);
            }

            if (!isFieldFilled()) {
                exchangePlayer();
                turnSkipCount = 0;
                e.player().increaseScore(e.currentWord().length());
                e.player().addWordToList();
                System.out.println("Игрок: " + e.player().name() + " добавил слово " + e.player().currentWord() + " Счёт игрока:" + e.player().score());

            } else {
                fireGameFinished(determineWinner());
            }
        }

        @Override
        public void cancel(PlayerActionEvent e) {
            Letter l = e.letter();
            l.unsetCell();
            l.unsetPlayer();
            fireCancel(e);
            field.setLettersNotChosen();
        }
    }

    private class MenuObserver implements MenuListener{
        @Override
        public void newGameStarted(MenuEvent e) {
            HashMap<String, Integer> size = new HashMap<>();
            int index = 5;
            for (String str : e.fieldSizes()) {
                size.put(str, index);
                index++;
            }

            if (e.selectedSize() != null && e.selectedDiff() != null) {
                int heightAndWidth = size.get(e.selectedSize());
                switch (e.selectedDiff()){
                    case "Легко":
                        alphabet = new Alphabet();
                        field = new GameField();
                        break;
                    case "Поле":
                        alphabet = new Alphabet();
                        field = new ComplicatedGameField();
                        break;
                    case "Алфавит":
                        alphabet = new ComplicatedAlphabet();
                        field = new GameField();
                        break;
                    case "Поле и алфавит":
                        alphabet = new ComplicatedAlphabet();
                        field = new ComplicatedGameField();
                }
                field.setSize(heightAndWidth, heightAndWidth);
                for (Player p : playerList){
                    p.setField(field);
                }
                startGame();
            } else { throw new IllegalArgumentException("Размер поля или сложность не заданы!"); }
        }
    }

        // ------------------------ Порождает события игры ----------------------------

        private ArrayList<GameListener> gameListenerList = new ArrayList<>();

        public void addGameListener(GameListener l) {
            gameListenerList.add(l);
        }

        public void removeGameListener(GameListener l) {
            gameListenerList.remove(l);
        }

        protected void fireGameFinished(Player winner) {
            GameEvent event = new GameEvent(this);
            event.setPlayer(winner);
            for (GameListener listener : gameListenerList) {
                listener.gameFinished(event);
            }
        }

        protected void fireCurrentLetterIsChosen(Player p) {
            GameEvent event = new GameEvent(this);
            event.setPlayer(p);
            for (GameListener listener : gameListenerList) {
                listener.currentLetterIsChosen(event);
            }
        }

    protected void fireDictionaryHasNotContainsWord(Player p) {
        GameEvent event = new GameEvent(this);
        event.setPlayer(p);
        for (GameListener listener : gameListenerList) {
            listener.dictionaryHasNotContainsWord(event);
        }
    }

    protected void fireWordHasBeenComposed(Player p) {
        GameEvent event = new GameEvent(this);
        event.setPlayer(p);
        for (GameListener listener : gameListenerList) {
            listener.wordHasBeenComposed(event);
        }
    }

        // --------------------- Порождает события, связанные с игроками -------------

    private ArrayList<PlayerActionListener> playerActionListenerList = new ArrayList<>();

    public void addPlayerActionListener(PlayerActionListener l) {
        playerActionListenerList.add(l);
    }

    public void removePlayerActionListener(PlayerActionListener l) {
        playerActionListenerList.remove(l);
    }

    protected void fireLetterIsPlaced(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.letterIsPlaced(e);
        }
    }

    protected void fireLetterIsReceived(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.letterIsReceived(e);
        }
    }

    protected void fireTurnIsSkipped(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.turnIsSkipped(e);
        }
    }

    protected void fireLetterOnFieldIsChosen(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.letterOnFieldIsChosen(e);
        }
    }

    protected void fireTurnIsOver(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.turnIsOver(e);
        }
    }

    protected void fireCancel(PlayerActionEvent e) {
        for (PlayerActionListener listener : playerActionListenerList) {
            listener.cancel(e);
        }
    }
}
