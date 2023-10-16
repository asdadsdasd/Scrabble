package model.view;

import model.entity.AbstractAlphabet;
import model.entity.ComplicatedAlphabet;
import model.entity.GameModel;
import model.events.GameEvent;
import model.events.GameListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlphabetWidget extends JPanel {
    private AbstractAlphabet alphabet;

    private ArrayList<JButton> buttonList = new ArrayList<>();

    private List<Character> activeLetters;

    public AlphabetWidget(AbstractAlphabet alphabet, GameModel model){
        model.addGameListener(new GameObserver());
        model.addPlayerActionListener(new PlayerObserver());
        this.alphabet = alphabet;
    }

    public List<JButton> buttons() {
        return Collections.unmodifiableList(buttonList);
    }

    public void buildLetterPanel(){
        List<Character> letters;
        if (alphabet.getClass() == ComplicatedAlphabet.class)
            letters = ((ComplicatedAlphabet)alphabet).returnAvailableLetters(-1);
        else letters = alphabet.returnAvailableLetters();

        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 220);
        setPreferredSize(dimension);
        JButton btn;
        for (Character letter : letters){
            btn = new JButton(Character.toString(letter));
            add(btn);
            buttonList.add(btn);
            btn.setEnabled(false);
            //btn.addActionListener(new GamePanel.ButtonClickListener()); //todo
        }
    }

    public void setEnabledButtons(boolean flag){
        for (JButton button : buttonList){
            button.setEnabled(flag);
        }
    }

    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            setEnabledButtons(false);
        }

        @Override
        public void letterIsReceived(PlayerActionEvent e) {

        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            setEnabledButtons(true);
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {

        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            setEnabledButtons(true);
        }

        @Override
        public void cancel(PlayerActionEvent e) {
            setEnabledButtons(true);
        }
    }

    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e) {
            setEnabledButtons(false);
        }

        @Override
        public void currentLetterIsChosen(GameEvent e) {

        }

        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {

        }

        @Override
        public void wordHasBeenComposed(GameEvent e) {

        }
    }
}
