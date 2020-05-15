package ir.amindannak.movies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ir.amindannak.movies.model.movie.MovieWithAllData;

@Dao
public interface MovieWithAllDataDao {

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieWithAllData> getMovie(int id);

    @Insert
    void insert(MovieWithAllData movie);
}
