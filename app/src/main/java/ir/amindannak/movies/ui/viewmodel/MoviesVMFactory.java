package ir.amindannak.movies.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.amindannak.movies.model.genre.Genre;

public class MoviesVMFactory implements ViewModelProvider.Factory {

    private Genre genre;

    public MoviesVMFactory(Genre genre) {
        this.genre = genre;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new MoviesVM(genre));
    }
}
