package model.view;

import model.entity.Cell;

import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {
    private final Map<Cell, CellWidget> cells = new HashMap<>();


    //Создать виджет клетки
    public CellWidget createCellWidget(Cell cell){
        if(cells.containsKey(cell)) return cells.get(cell);

        CellWidget cellWidget = new CellWidget(cell);
        cells.put(cell, cellWidget);
        return cellWidget;
    }
}
