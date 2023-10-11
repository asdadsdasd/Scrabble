package model.events;

import model.entity.Player;

import java.util.EventObject;

public class GameEvent extends EventObject {

    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player player() {
        return player;
    }

    public GameEvent(Object source){
        super(source);
    }
}
