package ir.amindannak.movies.ui.shared;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Utils {

    private static final String XML_DOUBLE_QUOTE = "&quot;"; // "
    private static final String XML_APOSTROPHE = "&apos;";   // '
    private static final String XML_AMPERSAND = "&amp;";     // &
    private static final String XML_LOWER_THAN = "&lt;";     // <
    private static final String XML_GREATER_THAN = "&gt;";   // >

    @Nullable
    public static String reviseString(@Nullable String s) {
        if (s == null) return null;
        s = s.replace("&", XML_AMPERSAND); // must be the first, if not, replaces & in thing like &quot
        s = s.replace("\"", XML_DOUBLE_QUOTE);
        return s;
    }

    @Nullable
    public static String putWordsInSeparateLines(@Nullable String string) {
        if (string == null) return null;
        List<String> list = Arrays.asList(string.replaceAll("\\s+", "").split(","));
        return putWordsInSeparateLines(list);
    }

    @Nullable
    public static String putWordsInSeparateLines(@Nullable List<String> list) {
        if (list == null) return null;
        StringBuilder builder = new StringBuilder();
        for (String s : list)
            builder.append(s.trim()).append("\n");
        return builder.substring(0, builder.length() - 1);
    }

    @Nullable
    public static String getCommaSeparatedString(@Nullable List<String> list) {
        if (list == null) return null;
        int size = list.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            builder.append(list.get(i)).append(", ");
        }
        builder.append(list.get(size - 1));
        return builder.toString();
    }

    public static String getString(Float f) {
        return String.format(Locale.getDefault(), "%.1f", f);
    }

    public static String getString(Integer i) {
        return String.format(Locale.getDefault(), "%d", i);
    }

    public enum MovieSort {
        IMDB_DESC, YEAR_DESC, YEAR_ASC
    }

}
