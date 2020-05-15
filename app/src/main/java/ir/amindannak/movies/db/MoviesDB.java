package ir.amindannak.movies.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ir.amindannak.movies.data.AppExecutors;
import ir.amindannak.movies.model.GenreMovieCrossRef;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.model.genre.GenreEnum;
import ir.amindannak.movies.model.genre.GenreUtils;
import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.model.movie.MoviesByGenreMetadata;
import ir.amindannak.movies.util.App;

@Database(
        version = 1,
        exportSchema = false,
        entities = {
                Genre.class,
                MovieWithBasicData.class,
                MovieWithAllData.class,
                GenreMovieCrossRef.class,
                MoviesByGenreMetadata.class
        }
)
@TypeConverters({GenreEnum.class, Converters.class})
public abstract class MoviesDB extends RoomDatabase {

    private static final String TAG = MoviesDB.class.getSimpleName();

    private static MoviesDB INSTANCE;
    private static final String DB_NAME = "movies.db";

    MoviesDB() {
    }

    public static synchronized MoviesDB getDB() {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(App.getApp(), MoviesDB.class, DB_NAME)
                    .addCallback(prepopulateCallback())
                    .build();
        }
        return INSTANCE;
    }

    private static Callback prepopulateCallback() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                AppExecutors.diskIO().execute(
                        () -> INSTANCE.genreDao().insert(GenreUtils.getAllGenres()));
            }
        };
    }

    public abstract GenreDao genreDao();

    public abstract MovieWithBasicDataDao movieWithBasicDataDao();

    public abstract MovieWithAllDataDao movieWithAllDataDao();

    public abstract GenreMovieCrossRefDao genreMovieCrossRefDao();

    public abstract MovieByGenreMetadataDao movieByGenreMetadataDao();

}
