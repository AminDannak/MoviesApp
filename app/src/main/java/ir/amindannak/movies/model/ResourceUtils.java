package ir.amindannak.movies.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import ir.amindannak.movies.util.App;

public class ResourceUtils {

    private static final String RES_TYPE_DRAWABLE = "drawable";
    private static final String RES_TYPE_STRING = "string";
    private static final String RES_TYPE_INTEGER = "integer";
    private static final String RES_PACKAGE = "ir.amindannak.movies";
    private static final int INVALID_ID = 0;

    private static int getResourceId(String name, String type) {
        return App.getRes().getIdentifier(name, type, RES_PACKAGE);
    }

    @Nullable
    public static String getStringFromResource(String name) {
        int id = getResourceId(name, RES_TYPE_STRING);
        if (id == INVALID_ID)
            return null;
        return App.getRes().getString(id);
    }

    @Nullable
    public static Drawable getDrawableFromResource(String name) {
        int id = getResourceId(name, RES_TYPE_DRAWABLE);
        if (id == INVALID_ID)
            return null;
        return App.getRes().getDrawable(id);
    }

}
