package model;

import model.view.GamePanel;

import javax.swing.*;

public class Scrabble {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }
}