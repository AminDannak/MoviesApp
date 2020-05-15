package ir.amindannak.movies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "genres_movies_cross_reference",
        primaryKeys = {"genre_id", "movie_id"},
        indices = {@Index(value = "genre_id"), @Index(value = "movie_id")}
)
public class GenreMovieCrossRef {

    @ColumnInfo(name = "genre_id")
    private int genreId;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    public GenreMovieCrossRef(int genreId, int movieId) {
        this.genreId = genreId;
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public int getMovieId() {
        return movieId;
    }

}
