package Tests.Letter;

import model.entity.*;
import model.view.GamePanel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class LetterTest {
    @Test
    public void testSetCell(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        GameField field = model.field();

        Letter letter = new Letter('ы');

        letter.setCell(field.cell(new Point(0,0)));

        Assert.assertEquals(field.cell(new Point(0,0)), letter.cell());
    }

    @Test
    public void testSetChosenTrue(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Letter letter = new Letter('ы');

        letter.setChosen(true);

        Assert.assertTrue(letter.isChosen());
    }

    @Test
    public void testSetChosenFalse(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Letter letter = new Letter('ы');

        letter.setChosen(false);

        Assert.assertFalse(letter.isChosen());
    }

    @Test
    public void testSetPlayer(){
        GameModel model = new GameModel(new GamePanel());
        model.startGame();

        Letter letter = new Letter('ы');

        letter.setPlayer(model.activePlayer());

        letter.setChosen(false);

        Assert.assertEquals(model.activePlayer(), letter.player());
    }
}
