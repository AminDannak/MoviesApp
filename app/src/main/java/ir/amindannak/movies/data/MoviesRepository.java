package ir.amindannak.movies.data;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ir.amindannak.movies.api.MoviesApiClient;
import ir.amindannak.movies.db.LocalCache;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.ui.shared.Utils;

public class MoviesRepository {

    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int PAGE_SIZE = 10;

    private MoviesApiClient apiClient;
    private LocalCache localCache;
    private static final PagedList.Config CONFIG;

    static {
        CONFIG = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .build();
    }

    public MoviesRepository() {
        apiClient = MoviesApiClient.getInstance();
        localCache = new LocalCache();
    }


    public LiveData<NetworkState> loadAndSaveMovieWithAllData(int id) {
        return apiClient.getMovieWithAllData(id, new LocalCache());
    }

    public LiveData<MovieWithAllData> getMovieWithAllData(int id) {
        return localCache.getMovie(id);
    }

    public LiveData<NetworkState> loadAndSaveMoviesData(Genre genre, @Nullable MoviesByGenreMetadata metadata) {
        Log.d("MoviesActivity", "loadAndSaveMoviesData: metadata: " + metadata);
        if (MoviesByGenreMetadata.isCompletelyLoaded(metadata))
            return new MutableLiveData<>(NetworkState.LOADED);

        int lastLoadedPage = 0;
        if (metadata != null) {
            lastLoadedPage = metadata.getCurrentPage();
        }
        return MoviesApiClient.getInstance().loadAndSaveMovies(localCache, genre, lastLoadedPage);
    }

    public LiveData<MoviesByGenreMetadata> getMoviesByGenreMetadata(Genre genre) {
        return localCache.getMetadata(genre);
    }


    public void stopLoadingMoviesData() {
        apiClient.stopLoadingMovies();
    }


    public LiveData<PagedList<MovieWithBasicData>> getMovies(Genre genre, Utils.MovieSort sort) {
        return new LivePagedListBuilder<>(
                localCache.getMoviesFactory(genre.getApiId(), sort),
                CONFIG
        ).build();
    }
}
