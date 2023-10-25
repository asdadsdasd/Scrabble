package Tests.GameModel;

import model.entity.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class GameModelTest {
    @Test
    public void testExchangePlayer(){
        GameModel model = new GameModel();
        model.startGame();

        Player notExpPlayer = model.activePlayer();
        model.exchangePlayer();

        Assert.assertNotEquals(model.activePlayer(), notExpPlayer);
    }

    @Test
    public void testSetLetterToActivePlayerHas(){
        GameModel model = new GameModel();
        model.startGame();

        model.setLetterToActivePlayer('щ');

        Assert.assertNotEquals(null, model.activePlayer().activeLetter());
    }

    @Test
    public void testSetLetterToActivePlayerHasNot(){
        GameModel model = new GameModel();
        model.startGame();

        model.setLetterToActivePlayer('Q');

        Assert.assertEquals(null, model.activePlayer().activeLetter());
    }
}
