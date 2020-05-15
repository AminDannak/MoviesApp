package ir.amindannak.movies.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailsVMFactory implements ViewModelProvider.Factory {

    private int movieApiId;

    public MovieDetailsVMFactory(int movieApiId) {
        this.movieApiId = movieApiId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new MovieDetailsVM(movieApiId));
    }
}
