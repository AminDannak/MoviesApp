package ir.amindannak.movies.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import ir.amindannak.movies.data.MoviesRepository;
import ir.amindannak.movies.data.NetworkState;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.ui.shared.Utils;

public class MoviesVM extends ViewModel {

    private static final String TAG = MoviesVM.class.getSimpleName();

    private MoviesRepository repository;
    private Genre genre;
    private LiveData<NetworkState> liveNetworkState;
    private LiveData<MoviesByGenreMetadata> liveMetadata;
    private final LiveData<PagedList<MovieWithBasicData>> movies;
    private MutableLiveData<Utils.MovieSort> liveSort;


    MoviesVM(Genre genre) {
        this.genre = genre;
        repository = new MoviesRepository();
        liveNetworkState = new MutableLiveData<>(null);
        liveMetadata = repository.getMoviesByGenreMetadata(genre);
        liveSort = new MutableLiveData<>(null);

        movies = Transformations.switchMap(liveSort,
                sort -> repository.getMovies(genre, sort));
    }

    public LiveData<PagedList<MovieWithBasicData>> getMovies() {
        return movies;
    }

    public void setSort(Utils.MovieSort sort) {
        liveSort.setValue(sort);
    }

    public LiveData<MoviesByGenreMetadata> getLiveMetadata() {
        return liveMetadata;
    }

    public LiveData<NetworkState> getLiveNetworkState() {
        return liveNetworkState;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }

    public LiveData<Integer> loadDataAndReportProgress() {
        Log.d(TAG, "loadDataAndReportProgress: called");
        LiveData<Integer> dataLoadingProgressPercentage = Transformations.map(liveMetadata, metadata -> {
            if (metadata == null) return 0;
            if (metadata.getCurrentPage() == metadata.getPageCount()) return 100;
            return 100 * metadata.getCurrentPage() * metadata.getPerPage() / metadata.getTotalCount();
        });
        liveNetworkState = repository.loadAndSaveMoviesData(genre, liveMetadata.getValue());
        return dataLoadingProgressPercentage;
    }

    public void stopObservingNetworkStateAndResetState(LifecycleOwner owner) {
        liveNetworkState.removeObservers(owner);
        liveNetworkState = new MutableLiveData<>(null);
    }

    public void stopLoadingMoviesData() {
        repository.stopLoadingMoviesData();
    }


}
