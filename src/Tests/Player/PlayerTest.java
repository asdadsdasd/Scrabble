package Tests.Player;

import model.entity.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class PlayerTest {
    @Test
    public void testIncreaseScore(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        player.increaseScore(5);

        Assert.assertEquals(5, player.score());
    }

    @Test
    public void testIncreaseScoreNothingHappened(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        player.increaseScore(-3);

        Assert.assertEquals(0, player.score());
    }

    @Test
    public void testAddWordToList(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        player.chooseLetter(new Point(1, 2));
        player.chooseLetter(new Point(2, 2));
        player.chooseLetter(new Point(3, 2));

        Assert.assertEquals(3, player.currentWord().length());
    }

    @Test
    public void testSetActiveLetter(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        Letter letter = new Letter('е');

        player.setActiveLetter(letter);

        Assert.assertEquals(letter, player.activeLetter());
    }

    @Test
    public void testSetLetterTo(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        Letter letter = new Letter('е');

        player.setActiveLetter(letter);

        player.setLetterTo(new Point(1, 1));

        Assert.assertEquals(model.field().cell(new Point(1, 1)).letter(), letter);
    }

    @Test
    public void testChooseLetter(){
        GameModel model = new GameModel();
        model.startGame();

        Player player = model.activePlayer();

        player.chooseLetter(new Point(1, 2));

        Assert.assertTrue(model.field().cell(new Point(1, 2)).letter().isChosen());
    }
}
