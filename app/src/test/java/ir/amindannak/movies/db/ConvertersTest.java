package ir.amindannak.movies.db;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConvertersTest {

    @Test
    public void Test_stringListToString_convertedStringListCanBeConvertedBack() {
        List<String> input = Arrays.asList("Drama", "Crime", "Fantasy");
        assertEquals(Converters.stringListFromString(Converters.stringListToString(input)), input);
    }

}
