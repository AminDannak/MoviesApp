package ir.amindannak.movies.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ir.amindannak.movies.R;
import ir.amindannak.movies.data.AppExecutors;
import ir.amindannak.movies.data.NetworkState;
import ir.amindannak.movies.data.Utils;
import ir.amindannak.movies.db.LocalCache;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesApiCallResponse;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.util.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class MoviesApiClient {

    private static final String TAG = MoviesApiClient.class.getSimpleName();
    private boolean cancelLoadingMoviesMsgReceived = false; // update this and act accordingly
    private boolean moviesAreBeingLoaded = false;

    private MoviesApi api;
    private static MoviesApiClient INSTANCE;

    private MoviesApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(MoviesApi.class);
    }

    public static synchronized MoviesApiClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesApiClient();
            Log.d(TAG, "getInstance: INSTANCE created");
        }
        return INSTANCE;
    }

    private MoviesApi getApi() {
        return api;
    }

    public void stopLoadingMovies() {
        if (moviesAreBeingLoaded)
            cancelLoadingMoviesMsgReceived = true;
    }

    public LiveData<NetworkState> getMovieWithAllData(int id, LocalCache localCache) {

        MutableLiveData<NetworkState> liveNetworkState = new MutableLiveData<>(NetworkState.LOADING);

        api.getMovieById(id).enqueue(new Callback<MovieWithAllData>() {

            @Override
            @EverythingIsNonNull
            public void onResponse(Call<MovieWithAllData> call, Response<MovieWithAllData> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: successful. code = " + response.code());
                    MovieWithAllData movie = response.body();
                    if (movie != null) {
                        Log.d(TAG, "onResponse: response body = " + movie);
                        localCache.insert(movie);
                        liveNetworkState.postValue(NetworkState.LOADED);
                    }
                } else {
                    Log.d(TAG, "onResponse: NOT successful. code = " + response.code());
                    liveNetworkState.postValue(
                            NetworkState.error(Utils.mapFailedHttpResponseCodeToMsg(response.code())));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<MovieWithAllData> call, Throwable t) {
                Log.d(TAG, "onFailure: msg: " + t.getMessage());
                liveNetworkState.postValue(NetworkState.error(App.getApp().getString(R.string.client_error_msg)));
            }
        });
        return liveNetworkState;
    }

    public LiveData<NetworkState> loadAndSaveMovies(LocalCache localCache, Genre genre, int lastLoadedApiPage) {
        Log.d(TAG, "loadAndSaveMovies: api page being requested for genre [" + genre + "] = " + lastLoadedApiPage + 1);
        MutableLiveData<NetworkState> liveNetworkState = new MutableLiveData<>(NetworkState.LOADING);
        AppExecutors.networkIO().submit(loadAndSaveMoviesTask(lastLoadedApiPage + 1,
                genre, localCache, networkState -> {
                    liveNetworkState.postValue(networkState);
                    Log.d(TAG, "loadAndSaveMovies: new State = " + networkState.getStatus());
                }));
        return liveNetworkState;

    }

    private Runnable loadAndSaveMoviesTask(int startingPage, Genre genre, LocalCache cache,
                                           NetworkState.NetworkStateUpdater networkStateUpdater) {
        return () -> {
            int page = startingPage;
            boolean shouldKeepLoading = true;
            moviesAreBeingLoaded = true;
            while (shouldKeepLoading) {
                shouldKeepLoading = loadAndSaveMovies(cache, genre, page, networkStateUpdater);
                page++;
            }
            moviesAreBeingLoaded = false;
        };
    }


    private boolean loadAndSaveMovies(LocalCache localCache, Genre genre, int apiPage,
                                      NetworkState.NetworkStateUpdater networkStateUpdater) {
        boolean shouldKeepLoading = true;
        try {
            Response<MoviesApiCallResponse> response =
                    MoviesApiClient.getInstance().getApi().getMoviesByGenre(genre.getApiId(), apiPage).execute();
            Log.d(TAG, "onResponse: code = " + response.code());
            if (response.isSuccessful()) {
                MoviesApiCallResponse body = response.body();
                if (body != null) {
                    MoviesByGenreMetadata metadata = body.getMetadata();
                    List<MovieWithBasicData> movies = body.getMovies();
                    Log.d(TAG, "onResponse: caching " + movies.size() + " movies");
                    Log.d(TAG, "onResponse: movies being cached: " + movies);
                    localCache.insertMovies(movies);
                    metadata.setGenreId(genre.getApiId());
                    Log.d(TAG, "onResponse: caching " + metadata);
                    localCache.insert(metadata, apiPage == 1);
                    if (MoviesByGenreMetadata.isCompletelyLoaded(metadata)) {
                        shouldKeepLoading = false;
                        networkStateUpdater.updateState(NetworkState.LOADED);
                    }
                    Log.d(TAG, "onResponse: lastLoadedApiPage = " + apiPage);
                }
            } else {
                Log.d(TAG, "onResponse: NOT successful. error body = " + response.errorBody());
                networkStateUpdater.updateState(NetworkState.error(Utils.mapFailedHttpResponseCodeToMsg(response.code())));
                shouldKeepLoading = false;
            }

        } catch (IOException | RuntimeException e) {
            Log.e(TAG, "onFailure: msg = " + e.getMessage());
            networkStateUpdater.updateState(NetworkState.error(App.getApp().getString(R.string.client_error_msg)));
            shouldKeepLoading = false;
            e.printStackTrace();
        }

        return considerPendingCancellation(shouldKeepLoading);
    }

    private boolean considerPendingCancellation(boolean shouldKeepLoading) {
        if (cancelLoadingMoviesMsgReceived) {
            cancelLoadingMoviesMsgReceived = false;
            return false;
        } else {
            return shouldKeepLoading;
        }
    }

}
