package model.view;

import model.entity.Alphabet;
import model.entity.Cell;
import model.entity.Dictionary;
import model.entity.GameModel;
import model.events.GameEvent;
import model.events.GameListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GamePanel extends JFrame {
    private FieldWidget fieldWidget;

    private AlphabetWidget alphabetWidget;

    private ActionButtonWidget actionButtonWidget;

    private WordListWidget wordListWidget;

    private  PlayerScoreWidget playerScoreWidget;

    private WidgetFactory widgetFactory = new WidgetFactory();

    private JMenuBar menu;
    private final String fileItems[] = new String[]{"New", "Exit"};

    private final String fieldSizes[] = new String[]{"5x5", "6x6", "7x7", "8x8"};

    private GameModel model = new GameModel();

    public GamePanel() {
        super();
        this.setTitle("Балда");

        //Представление должно реагировать на изменение состояния модели
        model.addGameListener(new GameObserver());

        setLayout(new BorderLayout());

        // Меню
        createMenu();
        setJMenuBar(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаем игровое поле
        this.fieldWidget = new FieldWidget(model.field(), widgetFactory, model);
        fieldWidget.createField();
        add(fieldWidget, BorderLayout.WEST);

        // Создаем панель, которую затем поместим на основную панель
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        // Создаем виджет, отображающий буквы алфавита
        this.alphabetWidget = new AlphabetWidget(new Alphabet(), model);
        alphabetWidget.buildLetterPanel();
        for (JButton button : alphabetWidget.buttons()) {
            button.addActionListener(new LetterClickListener());
        }
        centerPanel.add(alphabetWidget);

        // Создаем виджет, отображающий кнопки действий
        this.actionButtonWidget = new ActionButtonWidget(model);
        actionButtonWidget.buildActionButtonsPanel();
        centerPanel.add(actionButtonWidget);
        for (JButton button : actionButtonWidget.buttons()){
            button.addActionListener(new ActionButtonClickListener());
        }
        add(centerPanel, BorderLayout.CENTER);

        // Создаем панель, которую затем поместим на основную панель
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        eastPanel.setBorder(new EmptyBorder(0,10,0,10));

        // Создаем виджет, отображающий текущий счет игроков
        this.playerScoreWidget = new PlayerScoreWidget(model);
        playerScoreWidget.buildPlayerScorePane();
        eastPanel.add(playerScoreWidget);

        // Создаем виджет, отображающий списки слов, составленные игроками
        this.wordListWidget = new WordListWidget(model);
        wordListWidget.buildPlayerWordsBoard();
        eastPanel.add(wordListWidget);

        add(eastPanel, BorderLayout.EAST);

        pack();
        setResizable(false);
        setVisible(true);

    }

    // ----------------------------- Создаем меню ----------------------------------

    private void createMenu() {

        menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        for (int i = 0; i < fileItems.length; i++) {

            JMenuItem item = new JMenuItem(fileItems[i]);
            item.setActionCommand(fileItems[i].toLowerCase());
            item.addActionListener(new NewMenuListener());
            fileMenu.add(item);
        }
        fileMenu.insertSeparator(1);

        menu.add(fileMenu);
    }

    public class NewMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if ("exit".equals(command)) {
                System.exit(0);
            }
            if ("new".equals(command)) {
                if (applySettings()) {
                    model.startGame();
                    setStartCondition();
                }
            }
        }
    }

    private boolean applySettings() {
        HashMap<String, Integer> size = new HashMap<>();
        size.put("5x5", 5);
        size.put("6x6", 6);
        size.put("7x7", 7);
        size.put("8x8", 8);
        String in = (String) JOptionPane.showInputDialog(null,
                "Выберите разсер поля :",
                "Размер поля",
                JOptionPane.QUESTION_MESSAGE,
                null, fieldSizes, fieldSizes[0]);
        if (in != null) {
            int heightAndWidth = size.get(in);
            model.field().setSize(heightAndWidth, heightAndWidth);
            return true;
        }
        return false;
    }


    private void setStartCondition() {
        fieldWidget.rebuildField();
        for (Map.Entry<CellWidget, Point> entry : fieldWidget.cellsMap().entrySet()) {
            entry.getKey().addActionListener(new FieldClickListener());
        }
        fieldWidget.setEnabledButtons(false);
        alphabetWidget.setEnabledButtons(true);
        actionButtonWidget.turnOffAllButtons();
        actionButtonWidget.setEnabledButtonWithText(true, "Пропустить ход");
        wordListWidget.clearLists();
        playerScoreWidget.setStartSettings();
    }

    // ------------------------- Реагируем на действия игрока ----------------------

    private class FieldClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            CellWidget button = (CellWidget) e.getSource();
            button.setEnabled(false);

            // Ставим на поле букву текущего игрока
            Point p = fieldWidget.getPointByButton(button);
            for (Cell c : model.field().cells()) {
                if (c.position().equals(p)) {
                    if (c.letter() == null) {
                        button.setColorPlaced();
                        model.activePlayer().setLetterTo(p);
                    } else {
                        button.setColorChosen();
                        model.activePlayer().chooseLetter(p);
                    }
                }
            }
        }
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

    private class ActionButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String btnText = btn.getText();
            System.out.println(btnText);
            if (btnText.equals("Пропустить ход")) {
                model.activePlayer().skipTurn();
            } else if (btnText.equals("Завершить ход")) {
                fieldWidget.setEnabledButtons(false);
                model.activePlayer().endTurn();
            } else if (btnText.equals("Отмена")) {
                model.activePlayer().cancel();
            }
        }
    }

    // ------------------------- Реагируем на изменения модели ----------------------
        private class GameObserver implements GameListener {

            @Override
            public void gameFinished(GameEvent e) {
                if (e.player() != null) {
                    String str = "Победил игрок" + e.player().name();
                    JOptionPane.showMessageDialog(null, str, "Победа!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String str = "Ничья";
                    JOptionPane.showMessageDialog(null, str, "Ничья", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void currentLetterIsChosen(GameEvent e) {
            }

            @Override
            public void dictionaryHasNotContainsWord(GameEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Введённого слова нет в словаре. Добавить?", "Добавление слова",
                        JOptionPane.YES_NO_OPTION);
                switch (result) {
                    case 0:
                        Dictionary.addWord(e.player().currentWord());
                        e.player().endTurn();
                        break;
                    case 1, -1:
                        e.player().cancel();
                        break;
                }
            }

            @Override
            public void wordHasBeenComposed(GameEvent e) {
                JOptionPane.showMessageDialog(null, "Введённое вам слово уже было составлено", "Отмена", JOptionPane.INFORMATION_MESSAGE);
                e.player().cancel();
            }
        }
    }

