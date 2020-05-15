package ir.amindannak.movies.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import ir.amindannak.movies.model.movie.MovieWithBasicData;

@Dao
public interface MovieWithBasicDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<MovieWithBasicData> movies);

}
