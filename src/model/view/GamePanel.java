package model.view;

import model.entity.Alphabet;
import model.entity.ComplicatedAlphabet;
import model.entity.Dictionary;
import model.entity.GameModel;
import model.events.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GamePanel extends JFrame {
    private FieldWidget fieldWidget;

    private AlphabetWidget alphabetWidget;

    private ActionButtonWidget actionButtonWidget;

    private WordListWidget wordListWidget;

    private  PlayerScoreWidget playerScoreWidget;

    private JMenuBar menu;
    private final String fileItems[] = new String[]{"New", "Exit"};

    private final String fieldSizes[] = new String[]{"5x5", "6x6", "7x7", "8x8"};

    private final String difficultyLevels[] = new String[]{"Легко", "Поле", "Алфавит", "Поле и алфавит"};

    private String selectedSize = null;
    private String selectedDiff = null;

    private GameModel model;

    public GamePanel() {
        super();
        this.setTitle("Балда");

        this.model = new GameModel(this);

        //Представление должно реагировать на изменение состояния модели
        model.addGameListener(new GameObserver());

        setLayout(new BorderLayout());

        // Меню
        createMenu();
        setJMenuBar(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаем игровое поле
        this.fieldWidget = new FieldWidget(model.field(), model, this);
        fieldWidget.createField();
        add(fieldWidget, BorderLayout.WEST);

        // Создаем панель, которую затем поместим на основную панель
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        // Создаем виджет, отображающий буквы алфавита
        this.alphabetWidget = new AlphabetWidget(new ComplicatedAlphabet(), model, this);
        alphabetWidget.buildLetterPanel();

        centerPanel.add(alphabetWidget);

        // Создаем виджет, отображающий кнопки действий
        this.actionButtonWidget = new ActionButtonWidget(model, this);
        actionButtonWidget.buildActionButtonsPanel();
        centerPanel.add(actionButtonWidget);

        add(centerPanel, BorderLayout.CENTER);

        // Создаем панель, которую затем поместим на основную панель
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        eastPanel.setBorder(new EmptyBorder(0,10,0,10));

        // Создаем виджет, отображающий текущий счет игроков
        this.playerScoreWidget = new PlayerScoreWidget(model, this);
        playerScoreWidget.buildPlayerScorePane();
        eastPanel.add(playerScoreWidget);

        // Создаем виджет, отображающий списки слов, составленные игроками
        this.wordListWidget = new WordListWidget(model, this);
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
                applySettings();
            }
        }
    }

    private List<MenuListener> listeners = new ArrayList<>();

    public void addMenuListener(MenuListener l){ listeners.add(l); }

    public void removeMenuListener(MenuListener l){ listeners.remove(l); }

    private void fireNewGameStarted(){
        MenuEvent event = new MenuEvent(this);
        event.setSelectedDiff(this.selectedDiff);
        event.setSelectedSize(this.selectedSize);
        event.setFieldSizes(this.fieldSizes);
        for (MenuListener l : listeners){
            l.newGameStarted(event);
        }
    }

    private void applySettings() {

        JDialog settingsDialog = new JDialog();
        settingsDialog.setLayout(new GridLayout(3, 2));
        settingsDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JLabel label1 = new JLabel("Размер поля: ");
        JLabel label2 = new JLabel("Сложность: ");

        JComboBox<String> comboBoxSize = new JComboBox<>(fieldSizes);
        JComboBox<String> comboBoxDiff = new JComboBox<>(difficultyLevels);

        JButton button1 = new JButton("Ок");
        JButton button2 = new JButton("Отмена");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSize = (String) comboBoxSize.getSelectedItem();
                selectedDiff = (String) comboBoxDiff.getSelectedItem();

                System.out.println(selectedSize);
                System.out.println(selectedDiff);

                settingsDialog.dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose();
            }
        });

        settingsDialog.add(label1);
        settingsDialog.add(comboBoxSize);
        settingsDialog.add(label2);
        settingsDialog.add(comboBoxDiff);
        settingsDialog.add(button1);
        settingsDialog.add(button2);

        settingsDialog.pack();
        settingsDialog.setResizable(false);
        settingsDialog.setVisible(true);

        if(selectedDiff != null && selectedSize != null) {
            fireNewGameStarted();
            selectedSize = null;
            selectedDiff = null;
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
            public void currentLetterIsChosen(GameEvent e) {}

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

