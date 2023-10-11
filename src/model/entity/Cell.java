package model.entity;

import java.awt.*;

public class Cell {
    private Point coordinates;

    void setPosition(Point pos){
        coordinates = pos;
    }

    public Point position(){
        return (Point) coordinates.clone();
    }



    // --------- Клетка, принадлежит полю. Принадлежность задает само поле ------
    private GameField field;

    void setField(GameField f){
        field = f;
    }

    // --------------------- Буква, принадлежащая ячейке -----------------------

    private Letter letter;

    public void setLetter(Letter letter) {
        if(letter != null) {
            this.letter = letter;
            letter.setCell(this);
        }else
            this.letter = null;
    }

    public Letter letter(){
        return this.letter;
    }


    public boolean isAdjacentToLetterCell(){
        Point p = this.position();
        if (this.letter != null){
            return false;
        }
        if(field.cell(new Point(p.x + 1, p.y)) != null && field.cell(new Point(p.x + 1, p.y)).letter() != null){
            return true;
        } else if (field.cell(new Point(p.x - 1, p.y)) != null && field.cell(new Point(p.x - 1, p.y)).letter() != null) {
            return true;
        }else if (field.cell(new Point(p.x, p.y + 1)) != null && field.cell(new Point(p.x, p.y + 1)).letter() != null) {
            return true;
        }else if (field.cell(new Point(p.x, p.y - 1)) != null && field.cell(new Point(p.x, p.y - 1)).letter() != null) {
            return true;
        }
        return false;
    }
}
