package model.view;

import model.entity.GameModel;
import model.events.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionButtonWidget extends JPanel {
    private GameModel model;

    private List<String> actionButtons = Arrays.asList("Завершить ход", "Отмена", "Пропустить ход");

    private List<JButton> buttonList = new ArrayList<>();

    public ActionButtonWidget(GameModel model, GamePanel panel){
        model.addPlayerActionListener(new PlayerObserver());
        model.addGameListener(new GameObserver());
        panel.addMenuListener(new MenuObserver());
        this.model = model;
    }

    public void buildActionButtonsPanel(){
        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 40);

        setPreferredSize(dimension);

        JButton btn;
        for (String str : actionButtons){
            btn = new JButton(str);
            add(btn);
            btn.addActionListener(new ActionButtonClickListener());
            buttonList.add(btn);
            btn.setEnabled(false);
        }
    }

    private void turnOffAllButtons(){
        for (JButton button : buttonList){
            button.setEnabled(false);
        }
    }

    private void setEnabledButtonWithText(boolean flag, String buttonText){
        for (JButton button : buttonList){
            if(button.getText().equals(buttonText)){
                button.setEnabled(flag);
            }
        }
    }

    private class ActionButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String btnText = btn.getText();
            System.out.println(btnText);
            if (btnText.equals("Пропустить ход")) {
                model.activePlayer().skipTurn();
            } else if (btnText.equals("Завершить ход")) {
                model.activePlayer().endTurn();
            } else if (btnText.equals("Отмена")) {
                model.activePlayer().cancel();
            }
        }
    }

    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            setEnabledButtonWithText(true, "Отмена");
        }

        @Override
        public void letterIsReceived(PlayerActionEvent e) {

        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {

        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
        }

        @Override
        public void cancel(PlayerActionEvent e) {
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
        }
    }

    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e) {
            turnOffAllButtons();
        }

        @Override
        public void currentLetterIsChosen(GameEvent e) {
            setEnabledButtonWithText(true, "Завершить ход");
        }

        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {

        }

        @Override
        public void wordHasBeenComposed(GameEvent e) {

        }
    }

    private class MenuObserver implements MenuListener {
        @Override
        public void newGameStarted(MenuEvent e) {
            turnOffAllButtons();
            setEnabledButtonWithText(true, "Пропустить ход");
        }
    }
}
