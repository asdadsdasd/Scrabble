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
            if(cell.letter() != null)
                setText(String.valueOf(cell.letter().character()));
            //setBackground(Color.WHITE);

            setFocusable(false);
            setVisible(true);
        }
    }

    public void repaintButton(){
        if(cell.letter() != null){
            setText(String.valueOf(cell.letter().character()));
        } else {
            setText(null);
        }
        revalidate();
        repaint();
    }

    public void setEnabledIfHasLetter(){
        if (cell.letter() != null){
            setEnabled(true);
        } else {
            //setColorDefault();
            setEnabled(false);
        }
    }

    public void setColorPlaced(){
        setBackground(Color.GREEN);
    }

    public void setColorChosen(){
        setBackground(Color.CYAN);
    }

    public void setColorDefault(){
        setBackground(null);
    }
}
