package ir.amindannak.movies.db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import java.util.ArrayList;
import java.util.List;

import ir.amindannak.movies.data.AppExecutors;
import ir.amindannak.movies.model.GenreMovieCrossRef;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.ui.shared.Utils;

import static ir.amindannak.movies.model.genre.GenreUtils.GENRE_API_NAME_ID_MAP;

public class LocalCache {

    private static final String TAG = LocalCache.class.getSimpleName();

    private MovieByGenreMetadataDao metadataDao;
    private MovieWithBasicDataDao movieWithBasicDataDao;
    private GenreMovieCrossRefDao genreMovieCrossRefDao;
    private MovieWithAllDataDao movieWithAllDataDao;

    public LocalCache() {
        MoviesDB db = MoviesDB.getDB();
        metadataDao = db.movieByGenreMetadataDao();
        movieWithBasicDataDao = db.movieWithBasicDataDao();
        genreMovieCrossRefDao = db.genreMovieCrossRefDao();
        movieWithAllDataDao = db.movieWithAllDataDao();
    }


    public LiveData<MoviesByGenreMetadata> getMetadata(Genre genre) {
        return metadataDao.getMetadata(genre.getApiId());
    }


    public void insert(MoviesByGenreMetadata metadata) {
        AppExecutors.diskIO().submit(() -> metadataDao.insert(metadata));
    }

    /**
     * <P>Called to first insert {@code movies} in db, and then insert
     * {@link GenreMovieCrossRef}s in their table based on the values
     * in {@code movies} and {@link Genre}s table.</P>
     * <p>The order of doing so is important, since {@link GenreMovieCrossRef}'s table has
     * foreign keys to fields in both {@link Genre} and {@link MovieWithBasicData} tables'
     * columns. So before inserting {@link GenreMovieCrossRef}s, {@code movies} must be
     * inserted.</p>
     *
     * @param movies a {@link List} of {@link MovieWithBasicData} to be inserted in db.
     */
    public void insertMovies(List<MovieWithBasicData> movies) {
        List<GenreMovieCrossRef> genreMovieCrossRefs = getGenreMovieCrossRefs(movies);
        if (genreMovieCrossRefs == null)
            Log.e(TAG, "insertMovies: returned cross ref. is null! WTF??");
        AppExecutors.diskIO().submit(() -> {
            movieWithBasicDataDao.insert(movies);
            genreMovieCrossRefDao.insert(genreMovieCrossRefs);
        });
    }

    private List<GenreMovieCrossRef> getGenreMovieCrossRefs(List<MovieWithBasicData> movies) {
        Log.d(TAG, "getGenreMovieCrossRefs: ");
        if (movies == null) return null;
        List<GenreMovieCrossRef> crossRefs = new ArrayList<>();
        for (MovieWithBasicData movie : movies) {
            List<String> movieGenresApiNames = movie.getGenres();
            for (String name : movieGenresApiNames) {
                Integer genreApiId = GENRE_API_NAME_ID_MAP.get(name);
                if (genreApiId != null)
                    crossRefs.add(new GenreMovieCrossRef(genreApiId, movie.getApiId()));
                else
                    throw new IllegalStateException(name + " genre is missing from what GenreUtils class returns");
            }
        }
        return crossRefs;
    }

    public void insert(MoviesByGenreMetadata metadata, boolean isFirstLoad) {
        if (isFirstLoad)
            insert(metadata);
        else
            AppExecutors.diskIO().submit(() -> metadataDao.update(metadata));
    }

    public void insert(MovieWithAllData movie) {
        AppExecutors.diskIO().submit(() -> movieWithAllDataDao.insert(movie));
    }

    public LiveData<MovieWithAllData> getMovie(int id) {
        return movieWithAllDataDao.getMovie(id);
    }

    public DataSource.Factory<Integer, MovieWithBasicData> getMoviesFactory(Integer apiId, Utils.MovieSort sort) {
        if (sort == null) return genreMovieCrossRefDao.getMoviesFactory(apiId);
        switch (sort) {
            case IMDB_DESC:
                return genreMovieCrossRefDao.getMoviesFactoryDescendingImdb(apiId);
            case YEAR_DESC:
                return genreMovieCrossRefDao.getMoviesFactoryDescendingYear(apiId);
            case YEAR_ASC:
                return genreMovieCrossRefDao.getMoviesFactoryAscendingYear(apiId);
            default:
                return null;
        }
    }
}
