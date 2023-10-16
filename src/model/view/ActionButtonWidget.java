package model.view;

import model.entity.GameModel;
import model.events.GameEvent;
import model.events.GameListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionButtonWidget extends JPanel {
    private List<String> actionButtons = Arrays.asList("Завершить ход", "Отмена", "Пропустить ход");

    private List<JButton> buttonList = new ArrayList<>();

    public ActionButtonWidget(GameModel model){
        model.addPlayerActionListener(new PlayerObserver());
        model.addGameListener(new GameObserver());
    }

    public void buildActionButtonsPanel(){
        setLayout(new FlowLayout());
        Dimension dimension = new Dimension(250, 40);

        setPreferredSize(dimension);

        JButton btn;
        for (String str : actionButtons){
            btn = new JButton(str);
            add(btn);
            buttonList.add(btn);
            btn.setEnabled(false);
        }
    }

    public void turnOffAllButtons(){
        for (JButton button : buttonList){
            button.setEnabled(false);
        }
    }

    public List<JButton> buttons() {
        return Collections.unmodifiableList(buttonList);
    }

    public void setEnabledButtonWithText(boolean flag, String buttonText){
        for (JButton button : buttonList){
            if(button.getText().equals(buttonText)){
                button.setEnabled(flag);
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
}
