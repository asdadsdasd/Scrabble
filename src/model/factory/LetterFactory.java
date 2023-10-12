package model.factory;

import model.entity.Letter;

public class LetterFactory {
    public Letter createLetter(char ch){
        return new Letter(ch);
    }
}
