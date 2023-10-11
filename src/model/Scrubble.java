package model;

import model.entity.Dictionary;
import model.view.GamePanel;

import javax.swing.*;

public class Scrubble {
    public static void main(String[] args) {
        Dictionary.getDictionary();
        JFrame frame = new GamePanel();
        frame.setVisible(true);



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