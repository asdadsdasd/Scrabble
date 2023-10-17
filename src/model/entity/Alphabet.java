package model.entity;

import java.util.*;

public class Alphabet extends ComplicatedAlphabet{
    @Override
    public List<Character> returnAvailableLetters(){
        return Collections.unmodifiableList(letters);
    }
}
