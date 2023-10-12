package model.view;

import model.entity.Cell;

import javax.swing.*;
import java.awt.*;

public class CellWidget extends JButton {

    private Cell cell;

    public CellWidget(Cell cell){
        if(cell == null) throw new RuntimeException("В CellWidget передан null");
        else{
            this.cell = cell;
            setText(String.valueOf(cell.letter().character()));
            setBackground(Color.WHITE);
            setFocusable(false);
        }
    }

    public void repaintButton(){
        if(cell.letter() != null){
            setText(String.valueOf(cell.letter().character()));
        }
        repaint();
    }
}
