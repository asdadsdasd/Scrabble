package model.events;

import java.util.EventListener;

public interface MenuListener extends EventListener {
    public void newGameStarted(MenuEvent e);
}
