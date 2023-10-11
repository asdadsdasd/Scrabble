package model.entity;

public class Letter {

    // --------------------- Буква -----------------------
    private char character;

    public void setChar(char letter) {
        this.character = letter;
    }

    public char letter(){
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

    private boolean haveChosen = false;

    public boolean isChosen() {
        return haveChosen;
    }

    public void setChosen(boolean haveChosen) {
        this.haveChosen = haveChosen;
    }

    // Игрок, которому прнадлежит буква. Буква может быть нейтральной (не принадлежать никому) -

    private Player player = null;

    public Player player(){ return this.player; }

    public void setPlayer(Player p){ this.player = p; }

    public void unsetPlayer(){
        this.player = null;
    }
}
