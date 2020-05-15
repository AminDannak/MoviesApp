package ir.amindannak.movies.ui;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ir.amindannak.movies.ui.shared.Utils.putWordsInSeparateLines;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilsTest {

    @Test
    public void Test_putWordsInSeparateLines_nullInput() {
        assertNull(putWordsInSeparateLines((String) null));
        assertNull(putWordsInSeparateLines((List<String>) null));
    }

    @Test
    public void Test_putWordsInSeparateLines_withSpaces() {
        String input = "hey , you, there,hey  ";
        String expectedOutput = "hey\nyou\nthere\nhey";
        assertEquals(expectedOutput, putWordsInSeparateLines(input));
    }

    @Test
    public void Test_putWordsInSeparateLines_withoutSpaces() {
        String input = "hey,you,there,hey";
        String expectedOutput = "hey\nyou\nthere\nhey";
        assertEquals(expectedOutput, putWordsInSeparateLines(input));
    }

    @Test
    public void Test_putWordsInSeparateLines_stringListInput() {
        List<String> list = new ArrayList<>();
        list.add("hey");
        list.add("you");
        list.add("there");
        list.add("hey");
        String expectedOutput = "hey\nyou\nthere\nhey";
        assertEquals(expectedOutput, putWordsInSeparateLines(list));
    }

    @Test
    public void Test_putWordsInSeparateLines_stringListInput_withSpaces() {
        List<String> list = new ArrayList<>();
        list.add("hey ");
        list.add("  you");
        list.add("there  ");
        list.add(" hey ");
        String expectedOutput = "hey\nyou\nthere\nhey";
        assertEquals(expectedOutput, putWordsInSeparateLines(list));
    }

}
