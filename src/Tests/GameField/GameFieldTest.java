package Tests.GameField;

import model.entity.*;
import model.view.GamePanel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GameFieldTest {
    @Test
    public void testCell(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Cell actualCell = field.cell(new Point(0, 0));

        Assert.assertEquals(field.cells().get(0), actualCell);
    }

    @Test
    public void testCellsAdjacentToLetters(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        ArrayList<Cell> actualCellList = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            actualCellList.add(field.cell(new Point(i, 1)));
            actualCellList.add(field.cell(new Point(i, 3)));
        }

        Assert.assertTrue(new HashSet<>(field.cellsAdjacentToLetters()).equals(new HashSet<>(actualCellList)));
    }

    @Test
    public void testCellsAdjacentToLettersNull(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(0, field.cellsAdjacentToLetters(field.cells().get(0)).size());
    }

    @Test
    public void testCellsAdjacentToLettersNotNull(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(2, field.cellsAdjacentToLetters(field.cell(new Point(2, 2))).size());
    }

    @Test
    public void testLetterNull(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(null, field.letter(new Point(0, 0)));
    }

    @Test
    public void testLetterNotNull(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertNotEquals(null, field.letter(new Point(2, 2)));
    }

    @Test
    public void testContainsRangeTrue(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(true, field.containsRange(new Point(2, 2)));
    }

    @Test
    public void testContainsRangeFalse(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(false, field.containsRange(new Point(17, 2)));
    }

    @Test
    public void testLetters(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Assert.assertEquals(field.width(), field.letters().size());
    }
}
