package ir.amindannak.movies.ui.activity.movie_details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ir.amindannak.movies.R;
import ir.amindannak.movies.data.NetworkState;
import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.ui.activity.movies.MoviesActivity;
import ir.amindannak.movies.ui.shared.Animations;
import ir.amindannak.movies.ui.shared.LoadingFailedDialog;
import ir.amindannak.movies.ui.shared.NoInternetConnectionDialog;
import ir.amindannak.movies.ui.shared.Utils;
import ir.amindannak.movies.ui.viewmodel.MovieDetailsVM;
import ir.amindannak.movies.ui.viewmodel.MovieDetailsVMFactory;
import ir.amindannak.movies.util.InternetConnection;

import static ir.amindannak.movies.data.NetworkState.Status.FAILED;

public class MovieDetailsActivity extends AppCompatActivity implements
        InternetConnection.ConnectionCheckedListener,
        NoInternetConnectionDialog.InternetDisconnectionRetryHandler,
        LoadingFailedDialog.FailedLoadingRetryHandler {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private MovieDetailsVM viewModel;
    private ProgressBar progressBar;
    private View additionalDataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        progressBar = findViewById(R.id.pb_movie_details_loading);
        additionalDataContainer = findViewById(R.id.meta_container_additional_data);
        additionalDataContainer.setAlpha(0f);

        Intent intent = getIntent();
        MovieWithBasicData basicData;
        if (intent.hasExtra(MoviesActivity.EXTRA_KEY_MOVIE)) {
            basicData = (MovieWithBasicData) intent.getSerializableExtra(MoviesActivity.EXTRA_KEY_MOVIE);
            viewModel = new ViewModelProvider(this,
                    new MovieDetailsVMFactory(Objects.requireNonNull(basicData).getApiId()))
                    .get(MovieDetailsVM.class);
            fillBasicInfo(basicData);
        }

        reactToInternetConnectivity();
    }

    private void fillBasicInfo(MovieWithBasicData basicData) {
        setText(R.id.tv_movie_title, basicData.getTitle());
        setText(R.id.tv_imdb_rating, Utils.getString(basicData.getImdbRating()));
        setText(R.id.tv_release_year, basicData.getYear().toString());
        setText(R.id.tv_genres, Utils.getCommaSeparatedString(basicData.getGenres()));
    }

    private void reactToInternetConnectivity() {
        InternetConnection.isOnline(this, this);
    }

    @Override
    public void onConnectionChecked(boolean isOnline) {
        Log.d(TAG, "onConnectionChecked: " + isOnline);
        if (isOnline) {
            reactToDataAvailability();
        } else {
            showNoInternetDialog();
        }
    }

    private void reactToDataAvailability() {
        Log.d(TAG, "reactToDataAvailability: called");
        viewModel.getMovie().observe(this, movie -> {
            boolean wasLoadedBefore = movie != null;
            if (wasLoadedBefore) {
                Log.d(TAG, "reactToDataAvailability: removing movie live data observers");
                viewModel.getMovie().removeObservers(this);
                Log.d(TAG, "reactToDataAvailability: showing data");
                showData(movie);
            } else {
                Log.d(TAG, "reactToDataAvailability: loading data");
                loadData();
            }
        });
    }

    private void showData(MovieWithAllData movie) {
//        progressBar.setVisibility(View.INVISIBLE);
        Animations.fadeOut(progressBar).start();
        populateDetailViews(movie);
    }

    private void loadData() {
        LiveData<NetworkState> liveNetworkState = viewModel.loadMovie();
        liveNetworkState.observe(this, state -> {
            Log.d(TAG, "loadData: starting to observe network state");
            if (state == null) {
                Log.d(TAG, "loadData: network state is null");
                return;
            }
            NetworkState.Status status = state.getStatus();
            Log.d(TAG, "loadData: new network status :" + status);
            if (status.equals(FAILED)) {
                Log.d(TAG, "loadData: removing live network observer");
                liveNetworkState.removeObservers(MovieDetailsActivity.this);
                Log.d(TAG, "loadData: showing loading failed dialog");
                showLoadingFailedDialog(state.getMsg());
            }
        });
    }

    private void showLoadingFailedDialog(String failureMsg) {
        Log.d(TAG, "displayLoadingFailedDialog called");
        Bundle args = new Bundle();
        args.putString(LoadingFailedDialog.KEY_MSG, failureMsg);

        LoadingFailedDialog dialog = new LoadingFailedDialog();
        dialog.setCancelable(false);
        dialog.setArguments(args);
        dialog.setRetainInstance(true); // set it to false, rotate and see what happens
        dialog.show(getSupportFragmentManager(), LoadingFailedDialog.class.getSimpleName());
    }

    private void showNoInternetDialog() {
        Log.d(TAG, "showNoInternetDialog: called");
        DialogFragment dialog = new NoInternetConnectionDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), NoInternetConnectionDialog.class.getSimpleName());
    }

    private void populateDetailViews(MovieWithAllData movie) {
        setText(R.id.tv_director, movie.getDirector());
        setText(R.id.tv_actors, Utils.putWordsInSeparateLines(movie.getActors()));
        setText(R.id.tv_writer, movie.getWriter());
        setText(R.id.tv_movie_plot, movie.getPlot());
        setText(R.id.tv_awards, movie.getAwards());
        setText(R.id.tv_country, movie.getCountry());
        setText(R.id.tv_released, movie.getReleased());
        setText(R.id.tv_runtime, movie.getRuntime());
        showScreenshots(movie);
        Animations.fadeIn(additionalDataContainer).start();
    }

    private void showScreenshots(MovieWithAllData movie) {
        List<String> links = movie.getImagesLinks();
        if (links == null) {
            findViewById(R.id.layout_no_screenshots_provided).setVisibility(View.VISIBLE);
        } else {
            RecyclerView recyclerView = findViewById(R.id.rv_screenshots);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            ScreenshotAdapter adapter = new ScreenshotAdapter(links);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setText(@IdRes int textViewResId, String str) {
        ((TextView) findViewById(textViewResId)).setText(str);
    }


    @Override
    public void failedLoadingRetry() {
        reactToInternetConnectivity();
    }

    @Override
    public void internetConnectionRetry() {
        reactToInternetConnectivity();
    }
}
