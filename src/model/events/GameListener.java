package model.events;

import java.util.EventListener;

public interface GameListener extends EventListener {
    public void gameFinished(GameEvent e);

    public void playerExchanged(GameEvent e);

    public void currentLetterIsChosen(GameEvent e);

    public void dictionaryHasNotContainsWord(GameEvent e);

    public void wordHasBeenComposed(GameEvent e);
}
