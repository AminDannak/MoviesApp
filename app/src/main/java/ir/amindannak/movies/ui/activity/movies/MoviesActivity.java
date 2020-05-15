package ir.amindannak.movies.ui.activity.movies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

import ir.amindannak.movies.R;
import ir.amindannak.movies.data.NetworkState;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.ui.activity.genres.GenresActivity;
import ir.amindannak.movies.ui.activity.movie_details.MovieDetailsActivity;
import ir.amindannak.movies.ui.shared.Animations;
import ir.amindannak.movies.ui.shared.LoadingFailedDialog;
import ir.amindannak.movies.ui.shared.NoInternetConnectionDialog;
import ir.amindannak.movies.ui.shared.Utils;
import ir.amindannak.movies.ui.viewmodel.MoviesVM;
import ir.amindannak.movies.ui.viewmodel.MoviesVMFactory;
import ir.amindannak.movies.util.InternetConnection;

import static ir.amindannak.movies.data.NetworkState.Status.FAILED;
import static ir.amindannak.movies.data.NetworkState.Status.RUNNING;
import static ir.amindannak.movies.data.NetworkState.Status.SUCCESS;

public class MoviesActivity extends AppCompatActivity implements
        MovieAdapter.OnMovieClickListener,
        InternetConnection.ConnectionCheckedListener,
        NoInternetConnectionDialog.InternetDisconnectionRetryHandler,
        LoadingFailedDialog.FailedLoadingRetryHandler {

    private static final String TAG = MoviesActivity.class.getSimpleName();

    public static final String EXTRA_KEY_MOVIE = "key for movie with basic info";

    private MoviesVM viewModel;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ImageView genreImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setSupportActionBar(findViewById(R.id.toolbar));

        genreImage = findViewById(R.id.iv_genre_image);
        progressBar = findViewById(R.id.pb_movies_data_loading);
        recyclerView = findViewById(R.id.rv_movies_act);

        progressBar.setAlpha(0f);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null && intentExtras.containsKey(GenresActivity.EXTRA_KEY_GENRE)) {
            Genre genre = (Genre) intentExtras.get(GenresActivity.EXTRA_KEY_GENRE);
            if (genre != null) {
                viewModel = new ViewModelProvider(getViewModelStore(), new MoviesVMFactory(genre)).get(MoviesVM.class);
            }
        }

        prepareToolbar();
        reactToInternetConnectivity();
    }

    private void prepareToolbar() {
        Log.d(TAG, "prepareCollapsingToolbar: called");
        Genre genre = viewModel.getGenre();
        genreImage.setImageDrawable(genre.getImageDrawable());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout ctl = findViewById(R.id.toolbar_layout);
        ctl.setTitle(genre.getDisplayedName());
        ctl.setExpandedTitleColor(getResources().getColor(R.color.colorTransparent));
        ctl.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
    }

    private void reactToInternetConnectivity() {
        Log.d(TAG, "reactToInternetConnectivity: called");
        InternetConnection.isOnline(this, this);
    }

    @Override
    public void onConnectionChecked(boolean isOnline) {
        if (isOnline) {
            Log.d(TAG, "onConnectionChecked: ONLINE");
            reactToDataAvailability();
        } else {
            Log.d(TAG, "onConnectionChecked: NOT ONLINE");
            showNoInternetDialog();
        }
    }

    @Override
    public void internetConnectionRetry() {
        reactToInternetConnectivity();
    }

    @Override
    public void failedLoadingRetry() {
        reactToInternetConnectivity();
    }

    private void reactToDataAvailability() {
        Log.d(TAG, "reactToDataAvailability: called");
        viewModel.getLiveMetadata().observe(this, metadata -> {
            if (MoviesByGenreMetadata.isCompletelyLoaded(metadata)) {
                Log.d(TAG, "reactToDataAvailability: showing data");
                showData();
            } else {
                Log.d(TAG, "reactToDataAvailability: loading data");
                loadData();
            }
            viewModel.getLiveMetadata().removeObservers(MoviesActivity.this);
        });
    }

    private void showData() {
        Log.d(TAG, "showData: showing data");
        prepareToolbar();
        hideProgressIndicator();
        prepareRecyclerView();
    }

    private void loadData() {
        showProgressIndicator();
        loadDataAndShowProgress();
        viewModel.getLiveNetworkState().observe(this, state -> {
            Log.d(TAG, "loadData: network state change observed");
            if (state == null) {
                Log.d(TAG, "loadData: state is null. returning...");
                return;
            }
            NetworkState.Status status = state.getStatus();
            Log.d(TAG, "loadData: network status changed to [" + status + "]");
            if (status.equals(FAILED)) {
                showLoadingFailedDialog(state.getMsg());
            } else if (status.equals(SUCCESS)) {
                showData();
            }
            if (status != RUNNING) {
                Log.d(TAG, "loadData: removing live network state observer");
                viewModel.stopObservingNetworkStateAndResetState(this);
                viewModel.loadDataAndReportProgress().removeObservers(MoviesActivity.this);
            }
        });
    }

    private void hideProgressIndicator() {
        Animations.fadeOut(progressBar).start();
    }

    private void showProgressIndicator() {
        Animations.fadeIn(progressBar).start();
    }


    private void loadDataAndShowProgress() {
        viewModel.loadDataAndReportProgress().observe(this, percentage -> {
            if (percentage == null) return;
            updateProgress(percentage);
        });
    }

    private void updateProgress(Integer progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }


    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MovieAdapter rvAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(rvAdapter);
        Objects.requireNonNull(viewModel).getMovies().observe(this, newList -> {
            Log.d(TAG, "prepareMoviesRecyclerView: MoviesVM observed changes");
            rvAdapter.submitList(newList);
        });

    }

    private void showLoadingFailedDialog(String failureMsg) {
        Log.d(TAG, "displayLoadingFailedDialog called");
        Bundle args = new Bundle();
        args.putString(LoadingFailedDialog.KEY_MSG, failureMsg);

        LoadingFailedDialog dialog = new LoadingFailedDialog();
        dialog.setCancelable(false);
        dialog.setArguments(args);
        dialog.setRetainInstance(true);
        dialog.show(getSupportFragmentManager(), LoadingFailedDialog.class.getSimpleName());
    }

    private void showNoInternetDialog() {
        Log.d(TAG, "showNoInternetDialog called");
        DialogFragment dialog = new NoInternetConnectionDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), NoInternetConnectionDialog.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        viewModel.stopLoadingMoviesData();
        super.onBackPressed();
    }

    @Override
    public void onMovieClicked(MovieWithBasicData movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_KEY_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movies_sort, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_sort_imdb_desc:
                viewModel.setSort(Utils.MovieSort.IMDB_DESC);
                return true;
            case R.id.item_sort_year_descending:
                viewModel.setSort(Utils.MovieSort.YEAR_DESC);
                return true;
            case R.id.item_sort_year_ascending:
                viewModel.setSort(Utils.MovieSort.YEAR_ASC);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
