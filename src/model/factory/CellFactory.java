package model.factory;

import model.entity.Cell;

import java.awt.*;

public class CellFactory {
    public Cell createCell(Point point){
        return new Cell(point);
    }
}
