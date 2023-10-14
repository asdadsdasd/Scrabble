package model;

import model.view.GamePanel;

import javax.swing.*;

public class Scrabble {
    public static void main(String[] args) {
        //Dictionary.getDictionary();
        SwingUtilities.invokeLater(GamePanel::new);
//        JFrame frame = new GamePanel();
//        frame.setVisible(true);



//        Random random = new Random();
//        String tmp = Dictionary.getDictionary().getListOfWords().get(random.nextInt(Dictionary.getDictionary().getListOfWords().size()));
//        while(tmp.length() != 5){
//            tmp = Dictionary.getDictionary().getListOfWords().get(random.nextInt(Dictionary.getDictionary().getListOfWords().size()));
//        }
//        System.out.println(tmp);
//
//        Dictionary.addWord("ппппппппппп");
    }
}