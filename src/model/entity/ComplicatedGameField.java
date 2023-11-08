package model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ComplicatedGameField extends GameField{
    @Override
    public List<Cell> cellsAdjacentToLetters(){
        ArrayList<Cell> cells = new ArrayList<>();
        for(Cell c : cellList){
            if(c.isAdjacentToLetterCell())
                cells.add(c);
        }

        int numberOfElementsToRemove = cells.size() / 3;

        Random random = new Random();

        for (int i = 0; i < numberOfElementsToRemove; i++){
            cells.remove(random.nextInt(cells.size()));
        }
        return Collections.unmodifiableList(cells);
    }
}
