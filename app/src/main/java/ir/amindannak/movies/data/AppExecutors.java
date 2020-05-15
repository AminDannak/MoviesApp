package ir.amindannak.movies.data;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors INSTANCE;
    private final ExecutorService diskIO;
    private final ExecutorService networkIO;
    private final MainThreadExecutor mainThread;

    private AppExecutors(ExecutorService diskIO, ExecutorService networkIO, MainThreadExecutor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    private synchronized static AppExecutors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppExecutors(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool(),
                    new MainThreadExecutor()
            );
        }
        return INSTANCE;
    }

    public static ExecutorService diskIO() {
        return getInstance().diskIO;
    }

    public static ExecutorService networkIO() {
        return getInstance().networkIO;
    }

    public static Executor mainThread() {
        return getInstance().mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }


    }
}
