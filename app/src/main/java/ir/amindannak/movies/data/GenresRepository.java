package ir.amindannak.movies.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import ir.amindannak.movies.db.GenreDao;
import ir.amindannak.movies.db.MoviesDB;
import ir.amindannak.movies.model.genre.Genre;

public class GenresRepository {

    private GenreDao genreDao;

    public GenresRepository() {
        Log.d("", "GenresRepository: ");
        genreDao = MoviesDB.getDB().genreDao();
    }

    public LiveData<List<Genre>> getAllGenres() {
        return genreDao.getAllGenres();
    }

}
