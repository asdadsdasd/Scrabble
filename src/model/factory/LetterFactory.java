package model.factory;

import model.entity.Letter;

public class LetterFactory {
    public Letter createLetter(char ch){
        Letter letter = new Letter();
        letter.setChar(ch);
        return letter;
    }
}
