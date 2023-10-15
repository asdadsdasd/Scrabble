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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GamePanel extends JFrame {
    private JPanel mainPanel;

    private JPanel fieldPanel = new JPanel();

    private JPanel buttonPanel = new JPanel();

    private JPanel centerPanel = new JPanel();

    private FieldWidget fieldWidget;

    private AlphabetWidget alphabetWidget;

    private ActionButtonWidget actionButtonWidget;

    private WidgetFactory widgetFactory = new WidgetFactory();

    private JMenuBar menu;
    private final String fileItems[] = new String []{"New", "Exit"};

    private final String fieldSizes[] = new String[]{"5x5", "6x6", "7x7", "8x8"};

    private final int CELL_SIZE = 50;

    private GameModel model = new GameModel();

    public GamePanel() {
        super();
        this.setTitle("Балда");

        //Представление должно реагировать на изменение состояния модели
        model.addGameListener(new GameObserver());
        model.addPlayerActionListener(new PlayerObserver());

        //Главная панель, на которой будут распологаться все остальные
        //mainPanel = new JPanel();
       // mainPanel.setLayout(new BorderLayout());
        //mainPanel.setSize(1000, 1000);

        setLayout(new BorderLayout());

        // Меню
        createMenu();
        setJMenuBar(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Игровое поле
        //createField();
        //setEnabledField(false);
        //mainPanel.add(fieldPanel, BorderLayout.WEST);
        this.fieldWidget = new FieldWidget(model.field(), widgetFactory);
        fieldWidget.createField();
        add(fieldWidget, BorderLayout.WEST);

        // Создаем панель, которую затем поместим на основную панель
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        this.alphabetWidget = new AlphabetWidget(new Alphabet());
        alphabetWidget.buildLetterPanel();
        centerPanel.add(alphabetWidget);

        this.actionButtonWidget = new ActionButtonWidget();
        actionButtonWidget.buildActionButtonsPanel();
        centerPanel.add(actionButtonWidget);
        add(centerPanel, BorderLayout.CENTER);


        //buildButtonField();
        //mainPanel.add(buttonPanel, BorderLayout.CENTER);

        //drawLetterOnField();

        //setContentPane(mainPanel);
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
                if(applySettings()) {
                    model.startGame();
//                    createField();
//                    drawLetterOnField();
//                    buildButtonField();
//                    setEnabledField(false);
//                    setEnabledLetterButtons(true);
//                    setEnabledButtonWithText(true, "Пропустить ход");
                    //fieldWidget = new FieldWidget(model.field(), widgetFactory);
                    //mainPanel.add(fieldWidget, BorderLayout.WEST);
                    fieldWidget.repaintField();
                    //mainPanel.revalidate();
                    //mainPanel.repaint();
                }
            }
        }
    }

    private boolean applySettings(){
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
        if(in != null){
            int heightAndWidth =  size.get(in);
            model.field().setSize(heightAndWidth, heightAndWidth);
            return true;
        }
        return false;
    }


    // --------------------------- Отрисовываем поле ------------------------------

    private void createField(){
        fieldPanel.setDoubleBuffered(true);
        fieldPanel.setLayout(new GridLayout(model.field().height(), model.field().width()));

        Dimension fieldDimension = new Dimension(CELL_SIZE*8, CELL_SIZE*8);

        fieldPanel.setPreferredSize(fieldDimension);
        fieldPanel.setMinimumSize(fieldDimension);
        fieldPanel.setMaximumSize(fieldDimension);

        repaintField();
    }

    public void repaintField() {

        fieldPanel.removeAll();

        for (int row = 0; row < model.field().height(); row++) {
            for (int col = 0; col < model.field().width(); col++) {
                JButton button = new JButton("");
                button.setFocusable(false);
                fieldPanel.add(button);
                button.addActionListener(new FieldClickListener()); //todo
            }
        }

        fieldPanel.validate();
    }

    private Point buttonPosition(JButton btn){
        int index = 0;
        for (Component widget : fieldPanel.getComponents()){
            if(widget instanceof JButton){
                if (btn.equals((JButton)widget)){
                    break;
                }
                index++;
            }
        }

        int fieldWidth = model.field().width();
        return new Point(index%fieldWidth, index/fieldWidth);
    }

    private JButton getButton(Point pos){
        int index = model.field().width()*(pos.y) + (pos.x);

        for (Component widget : fieldPanel.getComponents()){
            if(widget instanceof JButton){
                if(index == 0){
                    return (JButton)widget;
                }
                index--;
            }
        }
        return null;
    }

    private void setEnabledField(boolean flag){
        Component comp[] = fieldPanel.getComponents();
        for (Component c : comp){
            c.setEnabled(flag);
        }
    }

    private void setEnabledFieldWithLetters(){
        for(Cell c : model.field().cells()){
            if(c.letter() != null){
                getButton(c.position()).setEnabled(true);
            }else{
                getButton(c.position()).setEnabled(false);
            }
        }
    }

    private void setEnabledFieldAdjacentToLetters(){
        setEnabledField(false);
        for(Cell c : model.field().cellsAdjacentToLetters()){
            getButton(c.position()).setEnabled(true);
        }
    }

    private void drawLetterOnField(){
        for (Cell c : model.field().cells()){
            if(c.letter() != null){
                JButton btn = getButton(c.position());
                btn.setText(String.valueOf(c.letter().character()));
                btn.revalidate();
                //btn.repaint();
            }else {
                JButton btn = getButton(c.position());
                btn.setText("");
                btn.revalidate();
                //btn.repaint();
            }
        }
    }

    private void setEnabledLetterButtons(boolean flag) {
        for (JButton btn : letterButtons){
            btn.setEnabled(flag);
        }
    }

    private void setEnabledButtonWithText(boolean flag, String buttonName){
        for (JButton btn : otherButtons){
            if (btn.getText().equals(buttonName))
                btn.setEnabled(flag);
        }
    }

    private void setEnabledOtherButtons(boolean flag){
        for (JButton btn : otherButtons){
            btn.setEnabled(flag);
        }
    }

    // ------------------------- Создаем оле с кнопками ----------------------

    private ArrayList<JButton> letterButtons = new ArrayList<>();

    private ArrayList<JButton> otherButtons = new ArrayList<>();

    private void buildButtonField() {
        buttonPanel.removeAll();

        buttonPanel.setLayout(new GridLayout(0, 1));
        String alphabet = ("абвгдеёжзийклмнопрстуфхцчшщъыьэюя");
        JButton btn;

        JPanel letterPanel = new JPanel(new FlowLayout());
        Dimension dimension = new Dimension(250, 220);
        letterPanel.setPreferredSize(dimension);
        for (char letter : alphabet.toCharArray()){
            btn = new JButton(Character.toString(letter));
            letterButtons.add(btn);
            letterPanel.add(btn);
            btn.addActionListener(new ButtonClickListener());
        }
        buttonPanel.add(letterPanel);
        JPanel otherButtonsPanel = new JPanel(new FlowLayout());
        dimension = new Dimension(250, 40);
        otherButtonsPanel.setPreferredSize(dimension);
        List<String> buttonNames = Arrays.asList("Завершить ход", "Отмена", "Пропустить ход");
        for (String str : buttonNames){
            otherButtonsPanel.add(createButton(str));
        }

        setEnabledLetterButtons(false);

        buttonPanel.add(otherButtonsPanel);
    }

    private JButton createButton(String buttonName){
        JButton btn = new JButton(buttonName);
        otherButtons.add(btn);
        btn.addActionListener(new ButtonClickListener());
        btn.setEnabled(false);
        return btn;
    }


    // ------------------------- Реагируем на действия игрока ----------------------

    private class FieldClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            JButton button = (JButton) e.getSource();
            button.setEnabled(false);

            // Ставим на поле букву текущего игрока
            Point p = buttonPosition(button);
            for (Cell c : model.field().cells()){
                if(c.position().equals(p)){
                    if(c.letter() == null) {
                        model.activePlayer().setLetterTo(p);
                    } else {
                        model.activePlayer().chooseLetter(p);
                    }
                }

            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String btnText = btn.getText();
            System.out.println(btnText);
            if (btnText.equals("Пропустить ход")){
                model.activePlayer().skipTurn();
            } else if (btnText.equals("Завершить ход")) {
                setEnabledField(false);
                model.activePlayer().endTurn();
            } else if (btnText.equals("Отмена")){
                model.activePlayer().cancel();
            }else{
                model.setLetterToActivePlayer(btnText.charAt(0));
            }
        }
    }

    private class PlayerObserver implements PlayerActionListener {

        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            drawLetterOnField();
            setEnabledLetterButtons(false);
            setEnabledFieldWithLetters();
            setEnabledButtonWithText(true, "Отмена");
        }

        @Override
        public void letterIsReceived(PlayerActionEvent e) {
            setEnabledFieldAdjacentToLetters();
        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            setEnabledField(false);
            setEnabledLetterButtons(true);
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
            drawLetterOnField();
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {
            //getButton(e.letter().cell().position()).setEnabled(false);
            setEnabledField(false);
            e.letter().setChosen(true); //todo как доделаю передвинуть в gameModel
            for(Cell c : model.field().cellsAdjacentToLetters(e.letter().cell())){
                if(!c.letter().isChosen())
                    getButton(c.position()).setEnabled(true);
            }
        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            setEnabledField(false);
            setEnabledLetterButtons(true);
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
        }

        @Override
        public void cancel(PlayerActionEvent e) {
            setEnabledField(false);
            setEnabledLetterButtons(true);
            setEnabledButtonWithText(false, "Завершить ход");
            setEnabledButtonWithText(false, "Отмена");
            drawLetterOnField();
            //todo при установке буквы и последующием нажатии отмены буква не пропадает с поля
        }
    }

    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e) {
            if(e.player() != null) {
                String str = "Победил игрок" + e.player().name();
                JOptionPane.showMessageDialog(null, str, "Победа!", JOptionPane.INFORMATION_MESSAGE);
            }else {
                String str = "Ничья";
                JOptionPane.showMessageDialog(null, str, "Ничья", JOptionPane.INFORMATION_MESSAGE);
            }

            setEnabledField(false);
            setEnabledLetterButtons(false);
            setEnabledOtherButtons(false);
        }

        @Override
        public void playerExchanged(GameEvent e) {

        }

        @Override
        public void currentLetterIsChosen(GameEvent e) {
            setEnabledButtonWithText(true, "Завершить ход");
        }

        @Override
        public void dictionaryHasNotContainsWord(GameEvent e) {
            int result = JOptionPane.showConfirmDialog(null,
                    "Введённого слова нет в словаре. Добавить?", "Добавление слова",
                    JOptionPane.YES_NO_OPTION);
            switch (result){
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
