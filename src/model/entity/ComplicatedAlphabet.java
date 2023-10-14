package model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComplicatedAlphabet extends AbstractAlphabet{

    public List<Character> returnAvailableLetters(int count){
        ArrayList<Character> copy = (ArrayList<Character>) letters.clone();
        if (count == -1)
            return copy;
        Random random = new Random();

        for (int i = 0; i < count; i++){
            int index = random.nextInt(copy.size());
            copy.remove(index);
        }
        return copy;
    }
}
