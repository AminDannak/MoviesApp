package ir.amindannak.movies.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ir.amindannak.movies.data.MoviesRepository;
import ir.amindannak.movies.data.NetworkState;
import ir.amindannak.movies.model.movie.MovieWithAllData;

public class MovieDetailsVM extends ViewModel {

    private MoviesRepository repository;
    private int movieApiId;
    private LiveData<MovieWithAllData> movieLiveData;

    MovieDetailsVM(int movieAPiId) {
        repository = new MoviesRepository();
        this.movieApiId = movieAPiId;
        movieLiveData = repository.getMovieWithAllData(movieApiId);
    }

    public LiveData<MovieWithAllData> getMovie() {
        return movieLiveData;
    }


    public LiveData<NetworkState> loadMovie() {
        return repository.loadAndSaveMovieWithAllData(movieApiId);
    }
}
