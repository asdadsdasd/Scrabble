package model.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameField {

    // ------------------------------ Клетки ---------------------------------------
    private ArrayList<Cell> cellList = new ArrayList<>();

    public List<Cell> cells(){
        return Collections.unmodifiableList(cellList);
    }

    Cell cell(Point pos){

        for(Cell obj : cellList)
        {
            if(obj.position().equals(pos))
            { return obj; }
        }

        return null;
    }

    void setCell(Point pos, Cell cell){
        // Удаляем старую ячейку
        removeCell(pos);

        // Связываем ячейку с полем
        cell.setField(this);
        cell.setPosition(pos);

        // Добавляем новую ячейку
        cellList.add(cell);
    }

    public void clear(){
        cellList.clear();
    }

    private void removeCell(Point pos){

        Cell obj = cell(pos);
        if(obj != null)     {
            cellList.remove(obj);
            obj.setField(null);
        }
    }

    //Возвращает все клетки, рядом с которыми есть клетка с буквой
    public List<Cell> cellsAdjacentToLetters(){
        ArrayList<Cell> cells = new ArrayList<>();
        for(Cell c : cellList){
            if(c.isAdjacentToLetterCell())
                cells.add(c);
        }
        return Collections.unmodifiableList(cells);
    }

    public List<Cell> neighbours(Cell cell){
        ArrayList<Cell> cells = new ArrayList<>();
        Point p = cell.position();
        if(cell(new Point(p.x + 1, p.y)) != null) {
                cells.add(cell(new Point(p.x + 1, p.y)));
        }if(cell(new Point(p.x - 1, p.y)) != null) {
                cells.add(cell(new Point(p.x - 1, p.y)));
        }if(cell(new Point(p.x, p.y + 1)) != null) {
                cells.add(cell(new Point(p.x, p.y + 1)));
        }if(cell(new Point(p.x, p.y - 1)) != null) {
                cells.add(cell(new Point(p.x, p.y - 1)));
        }
        return cells;
    }

    //Возвращает клетки-соседи с переданной, в которых есть буква
    public List<Cell> cellsAdjacentToLetters(Cell cell){
        ArrayList<Cell> cells = new ArrayList<>();
        for(Cell c : neighbours(cell)){
            if(c.letter() != null){
                cells.add(c);
            }
        }
        return Collections.unmodifiableList(cells);
    }

    public void setLettersNotChosen(){
        for (Letter l : letterList){
            l.setChosen(false);
        }
    }

    // ------------------------------ Буквы ---------------------------------------

    private ArrayList<Letter> letterList = new ArrayList<>();

    public List<Letter> letters() {
        letterList.clear();
        for (Cell c : cellList){
            Letter l = c.letter();
            if (l != null)
            letterList.add(c.letter());
        }
        return Collections.unmodifiableList(letterList);
    }

    public Letter letter(Point pos){
        Cell obj = cell(pos);
        if(obj != null) return obj.letter();
        return null;
    }

    public void setLetter(Point pos, Letter letter){
        Cell obj = cell(pos);
        if(obj != null){
            obj.setLetter(letter);
            letter.setCell(obj);
            letterList.add(letter);
        }
    }

    // ----------------------- Ширина и высота поля ------------------------------

    public GameField(){
        setSize(5, 5);
    }
    private int width;
    private int height;


    public void setSize(int width, int height) {

        this.width = width;
        this.height = height;

        // Удаляем все ячейки находящиеся вне поля
        for (Cell obj : cellList) {
            if(!containsRange(obj.position()) ) {
                cellList.remove(obj);
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean containsRange(Point p){
        return p.getX() >= 1 && p.getX() <= width &&
                p.getY() >= 1 && p.getY() <= height;
    }
}