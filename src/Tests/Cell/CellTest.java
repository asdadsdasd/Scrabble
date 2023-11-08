package Tests.Cell;

import model.entity.*;
import model.view.GamePanel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CellTest {
    @Test
    public void testSetLetter(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Cell cell = model.field().cells().get(0);

        Letter letter = new Letter('б');

        cell.setLetter(letter);

        Assert.assertEquals(letter, cell.letter());
    }

    @Test
    public void testIsAdjacentToLetterCellYes(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Assert.assertEquals(true, model.field().cells().get(17).isAdjacentToLetterCell());
    }

    @Test
    public void testIsAdjacentToLetterCellNo(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Assert.assertEquals(false, model.field().cells().get(12).isAdjacentToLetterCell());
    }
}
