package model.view;

import model.entity.GameModel;
import model.entity.Player;
import model.events.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordListWidget extends JPanel {
    private DefaultListModel<String> firstPlayerList;

    private DefaultListModel<String> secondPlayerList;

    private JList<String> firstPlayerJList;

    private JList<String> secondPlayerJList;

    public WordListWidget(GameModel model, GamePanel panel){
        model.addPlayerActionListener(new PlayerObserver());
        model.addGameListener(new GameObserver());
        panel.addMenuListener(new MenuObserver());
    }

    public void buildPlayerWordsBoard() {
        setLayout(new GridLayout(1, 2));
        setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        Dimension dimension = new Dimension(300, 250);

        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        firstPlayerList = new DefaultListModel<>();
        secondPlayerList = new DefaultListModel<>();
        firstPlayerJList = new JList<>(firstPlayerList);
        secondPlayerJList = new JList<>(secondPlayerList);

        JScrollPane scrollPane1 = new JScrollPane(firstPlayerJList);
        JScrollPane scrollPane2 = new JScrollPane(secondPlayerJList);

        add(scrollPane1);
        add(scrollPane2);

        setVisible(true);
    }


    private class PlayerObserver implements PlayerActionListener{
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
                if (e.player().name().equals("1"))
                    firstPlayerList.addElement(e.currentWord());
                else if (e.player().name().equals("2"))
                    secondPlayerList.addElement(e.currentWord());
            }
        }
        @Override
        public void cancel(PlayerActionEvent e) {}
    }

    private class GameObserver implements GameListener{
        @Override
        public void gameFinished(GameEvent e) {
            firstPlayerList.clear();
            secondPlayerList.clear();
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
            firstPlayerList.clear();
            secondPlayerList.clear();
        }
    }
}
