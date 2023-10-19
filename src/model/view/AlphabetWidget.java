package model.view;

import model.entity.ComplicatedAlphabet;
import model.entity.GameModel;
import model.events.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AlphabetWidget extends JPanel {
    private GameModel model;

    private ComplicatedAlphabet alphabet;

    private ArrayList<JButton> buttonList = new ArrayList<>();

    private List<Character> activeLetters;

    public AlphabetWidget(ComplicatedAlphabet alphabet, GameModel model, GamePanel panel){
        model.addGameListener(new GameObserver());
        model.addPlayerActionListener(new PlayerObserver());
        panel.addMenuListener(new MenuObserver());
        this.model = model;
        this.alphabet = alphabet;
    }

    public void buildLetterPanel(){
        List<Character> letters;
        letters = alphabet.returnLetterList();

        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 220);
        setPreferredSize(dimension);
        JButton btn;
        for (Character letter : letters){
            btn = new JButton(Character.toString(letter));
            add(btn);
            btn.addActionListener(new LetterClickListener());
            buttonList.add(btn);
            btn.setEnabled(false);
        }
        activeLetters = alphabet.returnAvailableLetters();
    }

    public void setAlphabet(ComplicatedAlphabet alphabet){
        this.alphabet = alphabet;
    }

    private void setEnabledButtons(boolean flag){
        for (Character ch : activeLetters){
            for (JButton button : buttonList){
                if(button.getText().equals(ch.toString()))
                    button.setEnabled(flag);
            }
        }
    }

    private void setEnabledAllButtons(boolean flag){
        for (JButton button : buttonList){
            button.setEnabled(flag);
        }
    }

    private void getNewButtons(){
        activeLetters = alphabet.returnAvailableLetters();
    }

    private class LetterClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String btnText = btn.getText();
            System.out.println(btnText);
            model.setLetterToActivePlayer(btnText.charAt(0));
        }
    }

    private class PlayerObserver implements PlayerActionListener{
        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            setEnabledAllButtons(false);
        }
        @Override
        public void letterIsReceived(PlayerActionEvent e) {}

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            getNewButtons();
            setEnabledButtons(true);
        }
        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {}

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            getNewButtons();
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
            setEnabledAllButtons(false);
        }
        @Override
        public void currentLetterIsChosen(GameEvent e) {}
        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {}
        @Override
        public void wordHasBeenComposed(GameEvent e) {}
    }

    private class MenuObserver implements MenuListener{
        @Override
        public void newGameStarted() {
            setEnabledAllButtons(false);
            getNewButtons();
            setEnabledButtons(true);
        }
    }
}
