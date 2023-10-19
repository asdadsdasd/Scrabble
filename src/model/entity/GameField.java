package model.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameField {

    // ------------------------------ Клетки ---------------------------------------
    private ArrayList<Cell> cellList = new ArrayList<>();

    public List<Cell> cells(){
        return Collections.unmodifiableList(cellList);
    }

    public Cell cell(Point pos){

        for(Cell obj : cellList)
        {
            if(obj.position().equals(pos))
            { return obj; }
        }

        return null;
    }

    void setCell(Cell cell){
        // Удаляем старую ячейку
        removeCell(cell.position());

        // Связываем ячейку с полем
        cell.setField(this);

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

        if (diffLevel == 2 && cells.size() > 2){
            int numberOfElementsToRemove = cells.size() / 3;

            Random random = new Random();

            for (int i = 0; i < numberOfElementsToRemove; i++){
                cells.remove(random.nextInt(cells.size()));
            }
        }
        return Collections.unmodifiableList(cells);
    }

    private List<Cell> neighbours(Cell cell){
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

    //Возвращает клетки-соседи с переданной, в которых есть буква, и которые еще не были выбраны
    public List<Cell> cellsAdjacentToLetters(Cell cell){
        ArrayList<Cell> cells = new ArrayList<>();
        for(Cell c : neighbours(cell)){
            if(c.letter() != null && !c.letter().isChosen()){
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

    private int diffLevel = 2;

    public void setDiffLevel(int level) {
        if(level != 1 && level != 2) throw new RuntimeException("В поле передан неправильный уровень сложности!");
        diffLevel = level;
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
        ArrayList<Cell> removableList = new ArrayList<>();
        for (Cell obj : cellList) {
            if(!containsRange(obj.position())) {
                removableList.add(obj);
            }
        }
        for (Cell c : removableList) {
            cellList.remove(c);
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean containsRange(Point p){
        return p.getX() >= 0 && p.getX() < width &&
                p.getY() >= 0 && p.getY() < height;
    }
}
