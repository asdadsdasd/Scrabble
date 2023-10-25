package model.entity;

import java.util.Objects;

public class Letter {

    // --------------------- Буква -----------------------
    private final char character;

    public Letter(char ch){
        this.character = ch;
    }

    public char character(){
        return this.character;
    }

    // --------------------- Клетка -----------------------
    private Cell cell = null;

    public Cell cell(){ return this.cell; }

    public void setCell(Cell cell) {
        if(this.cell == null){
            this.cell = cell;
            cell.setLetter(this);
        }
    }

    public void unsetCell(){
        this.cell.setLetter(null);
        this.cell = null;
    }

    //Была ли буква выбрана
    private boolean haveBeenChosen = false;

    public boolean isChosen() {
        return haveBeenChosen;
    }

    public void setChosen(boolean haveChosen) {
        this.haveBeenChosen = haveChosen;
    }

    // Игрок, которому прнадлежит буква. Буква может быть нейтральной (не принадлежать никому) -

    private Player player = null;

    public Player player(){ return this.player; }

    public void setPlayer(Player p){ this.player = p; }

    public void unsetPlayer(){
        this.player = null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Letter letter = (Letter) o;
        return character == letter.character && haveBeenChosen == letter.haveBeenChosen && Objects.equals(cell, letter.cell) && Objects.equals(player, letter.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, cell, haveBeenChosen, player);
    }
}
