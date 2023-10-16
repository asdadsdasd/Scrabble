package model.view;

import model.entity.Cell;
import model.entity.GameField;
import model.entity.GameModel;
import model.events.GameEvent;
import model.events.GameListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FieldWidget extends JPanel {

    private GameField field;

    private WidgetFactory factory;

    private final Map<CellWidget, Point> cellWidgetMap = new HashMap<>();

    private final int FIELD_SIZE = 400;

    public FieldWidget(GameField field, WidgetFactory factory, GameModel model) {
        model.addPlayerActionListener(new PlayerObserver());
        model.addGameListener(new GameObserver());
        this.field = field;
        this.factory = factory;
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
                add(button);
                cellWidgetMap.put(button, p);
            }
        }
        validate();
    }

    public Map<CellWidget, Point> cellsMap() {
        return Collections.unmodifiableMap(cellWidgetMap);
    }

    public Point getPointByButton(CellWidget cellWidget){
        return (Point) cellWidgetMap.get(cellWidget).clone();
    }

    public Map<Point, CellWidget> reverseCells() {
        HashMap<Point, CellWidget> reverseMap = new HashMap<>();
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }
        return reverseMap;
    }

    public void drawLettersOnField() {
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().repaintButton();
        }
    }

    public void setEnabledButtons(boolean flag) {
        for (CellWidget cell : cellWidgetMap.keySet()) {
            cell.setEnabled(flag);
        }
    }

    public void setEnabledCellsWithLetters() {
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().setEnabledIfHasLetter();
        }
    }

    public void setEnabledButtonsAdjacentToLetters() {
        setEnabledButtons(false);
        for (Cell c : field.cellsAdjacentToLetters()) {
            CellWidget widget = reverseCells().get(c.position());
            widget.setEnabled(true);
        }
    }

    public void setEnabledButtonsAdjacentToLetters(Cell cell) {
        Map<Point, CellWidget> map = reverseCells();
        for (Cell c : field.cellsAdjacentToLetters(cell)) {
            map.get(c.position()).setEnabled(true);
        }
    }

    public void setDefaultExceptChosenOne(Cell cell){
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            if (!entry.getValue().equals(cell.position())) {
                entry.getKey().setColorDefault();
            }
            else entry.getKey().setColorPlaced();
        }
    }

    public void setDefault(){
        for (Map.Entry<CellWidget, Point> entry : cellWidgetMap.entrySet()) {
            entry.getKey().setColorDefault();
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
            setEnabledButtonsAdjacentToLetters();
        }

        @Override
        public void turnIsSkipped(PlayerActionEvent e) {
            setEnabledButtons(false);
            setDefault();
            drawLettersOnField();
        }

        @Override
        public void letterOnFieldIsChosen(PlayerActionEvent e) {
            setEnabledButtons(false);
            setEnabledButtonsAdjacentToLetters(e.letter().cell());
            e.letter().setChosen(true);
        }

        @Override
        public void turnIsOver(PlayerActionEvent e) {
            setEnabledButtons(false);
            setDefault();
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
}
