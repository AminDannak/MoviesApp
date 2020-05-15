package ir.amindannak.movies.util;

import android.app.Application;
import android.content.res.Resources;

public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static App getApp() {
        return INSTANCE;
    }

    public static Resources getRes() {
        return INSTANCE.getResources();
    }
}
