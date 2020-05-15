package ir.amindannak.movies.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

import ir.amindannak.movies.model.GenreMovieCrossRef;
import ir.amindannak.movies.model.movie.MovieWithBasicData;


@Dao
public interface GenreMovieCrossRefDao {

    // id, title, posterLink, year, country, imdbRating, genres,
    // + [genre_id, movie_id, id, genre_name, displayedNameResourceName, drawableResourceName]

    @Query("SELECT * FROM movies_with_basic_data " +
            "INNER JOIN genres_movies_cross_reference ON genres_movies_cross_reference.movie_id = movies_with_basic_data.id " +
            "INNER JOIN genres ON genres.id = genres_movies_cross_reference.genre_id " +
            "WHERE genres.id = :genreId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    DataSource.Factory<Integer, MovieWithBasicData> getMoviesFactory(int genreId);

    @Query("SELECT * FROM movies_with_basic_data " +
            "INNER JOIN genres_movies_cross_reference ON genres_movies_cross_reference.movie_id = movies_with_basic_data.id " +
            "INNER JOIN genres ON genres.id = genres_movies_cross_reference.genre_id " +
            "WHERE genres.id = :genreId ORDER BY imdbRating DESC")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    DataSource.Factory<Integer, MovieWithBasicData> getMoviesFactoryDescendingImdb(int genreId);

    @Query("SELECT * FROM movies_with_basic_data " +
            "INNER JOIN genres_movies_cross_reference ON genres_movies_cross_reference.movie_id = movies_with_basic_data.id " +
            "INNER JOIN genres ON genres.id = genres_movies_cross_reference.genre_id " +
            "WHERE genres.id = :genreId ORDER BY year")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    DataSource.Factory<Integer, MovieWithBasicData> getMoviesFactoryAscendingYear(int genreId);

    @Query("SELECT * FROM movies_with_basic_data " +
            "INNER JOIN genres_movies_cross_reference ON genres_movies_cross_reference.movie_id = movies_with_basic_data.id " +
            "INNER JOIN genres ON genres.id = genres_movies_cross_reference.genre_id " +
            "WHERE genres.id = :genreId ORDER BY year DESC")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    DataSource.Factory<Integer, MovieWithBasicData> getMoviesFactoryDescendingYear(int genreId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GenreMovieCrossRef genreMovieCrossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<GenreMovieCrossRef> genreMovieCrossRefs);

}
