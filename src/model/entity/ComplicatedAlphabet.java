package model.entity;

import java.util.*;

public class ComplicatedAlphabet{
    protected ArrayList<Character> letters = new ArrayList<>(Arrays.asList(
            'а', 'б', 'в', 'г', 'д',
            'е', 'ё', 'ж', 'з', 'и',
            'й', 'к', 'л', 'м', 'н',
            'о', 'п', 'р', 'с', 'т',
            'у', 'ф', 'х', 'ц', 'ч',
            'ш', 'щ', 'ъ', 'ы', 'ь',
            'э', 'ю', 'я'));

    public List<Character> returnLetterList(){
        return Collections.unmodifiableList(letters);
    }

    public List<Character> returnAvailableLetters(){
        ArrayList<Character> copy = (ArrayList<Character>) letters.clone();
        Random random = new Random();

        for (int i = 0; i < 10; i++){
            int index = random.nextInt(copy.size());
            copy.remove(index);
        }
        return copy;
    }
}
