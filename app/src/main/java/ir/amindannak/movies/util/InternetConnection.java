package ir.amindannak.movies.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executor;

import ir.amindannak.movies.data.AppExecutors;

public class InternetConnection {


    public static void isOnline(ConnectionCheckedListener listener, LifecycleOwner lifecycleOwner) {
        Executor executor = AppExecutors.mainThread();
        AppExecutors.networkIO().execute(() -> {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                sock.connect(socketAddress, timeoutMs);
                sock.close();
                executor.execute(() -> {
                    if (lifecycleOwner.getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED))
                        listener.onConnectionChecked(true);
                });
            } catch (IOException e) {
                executor.execute(() -> {
                    if (lifecycleOwner.getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED))
                        listener.onConnectionChecked(false);
                });
            }
        });

    }

    @FunctionalInterface
    public interface ConnectionCheckedListener {
        void onConnectionChecked(boolean isOnline);
    }

}
