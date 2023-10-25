package Tests.Alphabet;

import model.entity.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class AlphabetTest {
    @Test
    public void testReturnLetterList(){
        Alphabet alphabet = new Alphabet();

        ArrayList<Character> letters = new ArrayList<>(Arrays.asList(
                'а', 'б', 'в', 'г', 'д',
                'е', 'ё', 'ж', 'з', 'и',
                'й', 'к', 'л', 'м', 'н',
                'о', 'п', 'р', 'с', 'т',
                'у', 'ф', 'х', 'ц', 'ч',
                'ш', 'щ', 'ъ', 'ы', 'ь',
                'э', 'ю', 'я'));

        Assert.assertEquals(letters, alphabet.returnLetterList());
    }

    @Test
    public void testReturnAvailableLettersAlphabet(){
        Alphabet alphabet = new Alphabet();

        ArrayList<Character> letters = new ArrayList<>(Arrays.asList(
                'а', 'б', 'в', 'г', 'д',
                'е', 'ё', 'ж', 'з', 'и',
                'й', 'к', 'л', 'м', 'н',
                'о', 'п', 'р', 'с', 'т',
                'у', 'ф', 'х', 'ц', 'ч',
                'ш', 'щ', 'ъ', 'ы', 'ь',
                'э', 'ю', 'я'));

        Assert.assertEquals(letters, alphabet.returnLetterList());
    }

    @Test
    public void testReturnAvailableLettersComplicatedAlphabet(){
        ComplicatedAlphabet alphabet = new ComplicatedAlphabet();

        Assert.assertEquals(23, alphabet.returnAvailableLetters().size());
    }

    @Test
    public void testCreateLetter(){
        Alphabet alphabet = new Alphabet();
        Letter letter = alphabet.createLetter('е');

        Letter letterCopy = new Letter('е');
        Assert.assertTrue(letter.equals(letterCopy));
    }
}
