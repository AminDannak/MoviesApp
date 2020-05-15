package ir.amindannak.movies.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import ir.amindannak.movies.R;
import ir.amindannak.movies.ui.activity.genres.GenresActivity;
import ir.amindannak.movies.ui.shared.NoInternetConnectionDialog;
import ir.amindannak.movies.util.InternetConnection;

public class SplashScreenActivity extends AppCompatActivity
        implements NoInternetConnectionDialog.InternetDisconnectionRetryHandler,
        InternetConnection.ConnectionCheckedListener {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    public static final long SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reactBasedOnInternetConnectivity();
    }

    private void reactBasedOnInternetConnectivity() {
        Log.d(TAG, "reactToInternetConnectivityStatus called");
        InternetConnection.isOnline(this, this);
    }

    private void displayNoInternetDialog() {
        Log.d(TAG, "displayNoInternetDialog called");
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NoInternetConnectionDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), NoInternetConnectionDialog.class.getSimpleName());
    }

    private void displaySplashAndFinish() {
        Log.d(TAG, "displaySplashAndFinish called");
        Intent intent = new Intent(this, GenresActivity.class);
        new Handler().postDelayed(
                () -> {
                    startActivity(intent);
                    finish();
                }
                , SPLASH_DURATION);
    }

    @Override
    public void onConnectionChecked(boolean isOnline) {
        if (isOnline) {
            displaySplashAndFinish();
        } else {
            displayNoInternetDialog();
        }
    }

    @Override
    public void internetConnectionRetry() {
        Log.d(TAG, "onRetry called");
        reactBasedOnInternetConnectivity();
    }
}
