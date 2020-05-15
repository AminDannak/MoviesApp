package ir.amindannak.movies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;

@Dao
public interface MovieByGenreMetadataDao {

    @Query("SELECT * FROM metadata WHERE genreId = :genreId")
    LiveData<MoviesByGenreMetadata> getMetadata(int genreId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MoviesByGenreMetadata metadata);

    @Update
    void update(MoviesByGenreMetadata metadata);
}
