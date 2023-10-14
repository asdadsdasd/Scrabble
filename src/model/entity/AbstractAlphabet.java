package model.entity;

import java.util.*;

public abstract class AbstractAlphabet {
    protected ArrayList<Character> letters = new ArrayList<>(Arrays.asList(
            'а', 'б', 'в', 'г', 'д',
            'е', 'ё', 'ж', 'з', 'и',
            'й', 'к', 'л', 'м', 'н',
            'о', 'п', 'р', 'с', 'т',
            'у', 'ф', 'х', 'ц', 'ч',
            'ш', 'щ', 'ъ', 'ы', 'ь',
            'э', 'ю', 'я'));

    public List<Character> returnAvailableLetters(){
        return Collections.unmodifiableList(letters);
    }
}
