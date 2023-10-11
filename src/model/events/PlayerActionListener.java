package model.events;

import java.util.EventListener;

public interface PlayerActionListener extends EventListener {
    void letterIsPlaced(PlayerActionEvent e);

    void letterIsReceived(PlayerActionEvent e);

    void turnIsSkipped(PlayerActionEvent e);

    void letterOnFieldIsChosen(PlayerActionEvent e);

    void turnIsOver(PlayerActionEvent e);

    void cancel(PlayerActionEvent e);
}
