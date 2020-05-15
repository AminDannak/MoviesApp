package ir.amindannak.movies.db;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// converter methods must be public. ignore the warnings
class Converters {

    private static final Gson GSON = new Gson();

    @TypeConverter
    @Nullable
    public static String stringListToString(List<String> list) {
        if (list == null) return null;
        return GSON.toJson(list);
    }

    @TypeConverter
    @Nullable
    public static List<String> stringListFromString(String string) {
        if (string == null) return null;
        Type targetType = new TypeToken<List<String>>() {
        }.getType();
        return GSON.fromJson(string, targetType);
    }

}
