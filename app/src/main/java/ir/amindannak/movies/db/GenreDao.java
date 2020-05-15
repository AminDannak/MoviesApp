package ir.amindannak.movies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.amindannak.movies.model.genre.Genre;

@Dao
public interface GenreDao {

    @Query("SELECT * FROM genres")
    LiveData<List<Genre>> getAllGenres();

    @Insert
    void insert(List<Genre> genres);

}
