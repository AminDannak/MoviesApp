package ir.amindannak.movies.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ir.amindannak.movies.data.GenresRepository;
import ir.amindannak.movies.model.genre.Genre;

public class GenresVM extends ViewModel {

    private LiveData<List<Genre>> allGenres;

    public GenresVM() {
        GenresRepository repository = new GenresRepository();
        allGenres = repository.getAllGenres();
    }

    public LiveData<List<Genre>> getAllGenres() {
        return allGenres;
    }


}
