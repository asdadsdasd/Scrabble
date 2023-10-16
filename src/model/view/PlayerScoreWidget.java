package model.view;

import model.entity.GameModel;
import model.events.*;

import javax.swing.*;
import java.awt.*;

public class PlayerScoreWidget extends JPanel {
    private int firstPlayerScore = 0;

    private int secondPlayerScore = 0;

    private JLabel firstPlayerLabel = new JLabel();

    private JLabel secondPlayerLabel = new JLabel();

    public PlayerScoreWidget(GameModel model, GamePanel panel){
        model.addGameListener(new GameObserver());
        model.addPlayerActionListener(new PlayerObserver());
        panel.addMenuListener(new MenuObserver());
    }

    public void buildPlayerScorePane(){
        setLayout(new GridLayout(1, 2));
        setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        Dimension dimension = new Dimension(300, 50);

        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        firstPlayerLabel.setText("Счет первого игрока: " + firstPlayerScore);
        secondPlayerLabel.setText("Счёт второго игрока: " + secondPlayerScore);

        add(firstPlayerLabel);
        add(secondPlayerLabel);

        setVisible(true);
    }

    private class PlayerObserver implements PlayerActionListener {
        @Override
        public void letterIsPlaced(PlayerActionEvent e) {}
        @Override
        public void letterIsReceived(PlayerActionEvent e) {}
        @Override
        public void turnIsSkipped(PlayerActionEvent e) {}
        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {}
        @Override
        public void turnIsOver(PlayerActionEvent e) {
            if (e.currentWord() != null && !e.currentWord().isEmpty()) {
                if (e.player().name().equals("1")) {
                    firstPlayerScore += e.currentWord().length();
                    firstPlayerLabel.setText("Счет первого игрока: " + firstPlayerScore);
                }
                else if (e.player().name().equals("2")) {
                    secondPlayerScore += e.currentWord().length();
                    secondPlayerLabel.setText("Счёт второго игрока: " + secondPlayerScore);
                }
            }
        }
        @Override
        public void cancel(PlayerActionEvent e) {}
    }

    private class GameObserver implements GameListener {
        @Override
        public void gameFinished(GameEvent e) {
            firstPlayerScore = 0;
            secondPlayerScore = 0;
        }
        @Override
        public void currentLetterIsChosen(GameEvent e) {}
        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {}
        @Override
        public void wordHasBeenComposed(GameEvent e) {}
    }

    private class MenuObserver implements MenuListener {
        @Override
        public void newGameStarted() {
            firstPlayerScore = 0;
            secondPlayerScore = 0;
            firstPlayerLabel.setText("Счет первого игрока: " + firstPlayerScore);
            secondPlayerLabel.setText("Счёт второго игрока: " + secondPlayerScore);
        }
    }
}
