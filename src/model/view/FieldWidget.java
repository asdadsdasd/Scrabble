package model.view;

import model.entity.Cell;
import model.entity.GameField;
import model.entity.GameModel;
import model.events.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FieldWidget extends JPanel {
    private GameModel model;

    private GameField field;

    private WidgetFactory factory = new WidgetFactory();

    private final Map<CellWidget, Point> cellWidgetMap = new HashMap<>();

    private ArrayList<CellWidget> activeCells = new ArrayList<>();

    private final int FIELD_SIZE = 400;

    public FieldWidget(GameField field, GameModel model, GamePanel panel) {
        model.addPlayerActionListener(new PlayerObserver());
        model.addGameListener(new GameObserver());
        panel.addMenuListener(new MenuObserver());
        this.model = model;
        this.field = field;
    }

    public void createField() {
        setLayout(new GridLayout(field.height(), field.width()));

        Dimension dimension = new Dimension(FIELD_SIZE, FIELD_SIZE);

        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        setVisible(true);
    }

    public void rebuildField() {
        removeAll();
        cellWidgetMap.clear();

        setLayout(new GridLayout(field.height(), field.height()));

        for (int row = 0; row < field.height(); row++) {
            for (int col = 0; col < field.width(); col++) {
                Point p = new Point(col, row);
                CellWidget button = factory.createCellWidget(field.cell(p));
                button.addActionListener(new FieldClickListener());
                add(button);
                cellWidgetMap.put(button, p);
            }
        }
        validate();
    }

    private Point getPointByButton(CellWidget cellWidget){
        return (Point) cellWidgetMap.get(cellWidget).clone();
    }

    private Map<Point, CellWidget> reverseCells() {
        HashMap<Point, CellWidget> reverseMap = new HashMap<>();
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }
        return reverseMap;
    }

    private void drawLettersOnField() {
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().repaintButton();
        }
    }

    private void setEnabledButtons(boolean flag) {
        for (CellWidget cell : cellWidgetMap.keySet()) {
            cell.setEnabled(flag);
        }
    }

    private void setEnabledCellsWithLetters() {
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().setEnabledIfHasLetter();
        }
    }

    private void getNewActiveButtons() {
        setEnabledButtons(false);
        activeCells.clear();
        for (Cell c : field.cellsAdjacentToLetters()) {
            CellWidget widget = reverseCells().get(c.position());
            activeCells.add(widget);
        }
    }

    private void setEnabledActiveButtons(boolean flag){
        for (CellWidget w : activeCells){
            w.setEnabled(flag);
        }
    }

    private void setEnabledCellsAdjacentToLetters(Cell cell) {
        Map<Point, CellWidget> map = reverseCells();
        for (Cell c : field.cellsAdjacentToLetters(cell)) {
            map.get(c.position()).setEnabled(true);
        }
    }

    private void setDefaultExceptChosenOne(Cell cell){
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            if (!entry.getValue().equals(cell.position())) {
                entry.getKey().setColorDefault();
            }
            else entry.getKey().setColorPlaced();
        }
    }

    private void setDefault(){
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().setColorDefault();
        }
    }

    private class FieldClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            CellWidget button = (CellWidget) e.getSource();
            button.setEnabled(false);

            // Ставим на поле букву текущего игрока
            Point p = getPointByButton(button);
            for (Cell c : field.cells()) {
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

    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void letterIsPlaced(PlayerActionEvent e) {
            drawLettersOnField();
            setEnabledCellsWithLetters();
            setDefaultExceptChosenOne(e.letter().cell());
        }

        @Override
        public void letterIsReceived(PlayerActionEvent e) {
            setEnabledActiveButtons(true);
        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            setEnabledButtons(false);
            setDefault();
            drawLettersOnField();
            getNewActiveButtons();
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {
            setEnabledButtons(false);
            setEnabledCellsAdjacentToLetters(e.letter().cell());
        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            setEnabledButtons(false);
            setDefault();
            getNewActiveButtons();
        }

        @Override
        public void cancel(PlayerActionEvent e) {
            setEnabledButtons(false);
            setDefault();
            drawLettersOnField();
        }
    }

    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e) {
            setDefault();
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

    private class MenuObserver implements MenuListener {
        @Override
        public void newGameStarted() {
            rebuildField();
            setEnabledButtons(false);
            getNewActiveButtons();
        }
    }
}
