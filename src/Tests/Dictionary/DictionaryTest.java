package Tests.Dictionary;

import model.entity.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DictionaryTest {
    @Test
    public void testIsContainsWordHas(){
        Assert.assertTrue(Dictionary.isContainsWord("письмо"));
    }

    @Test
    public void testIsContainsWordHasNot(){
        Assert.assertFalse(Dictionary.isContainsWord("письмоъыъ"));
    }

    @Test
    public void testGetRandomWordContains(){
        Assert.assertEquals(7, Dictionary.getRandomWord(7).length());
    }
}
