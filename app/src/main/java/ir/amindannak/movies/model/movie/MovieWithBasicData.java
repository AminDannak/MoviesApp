package ir.amindannak.movies.model.movie;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "movies_with_basic_data")
public class MovieWithBasicData implements Serializable {

    private static final String TAG = MovieWithBasicData.class.getSimpleName();

    // since it's unique, we don't need to generate the PK
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int apiId;
    private String title;
    @SerializedName("poster")
    private String posterLink;
    private Integer year;
    private String country;
    @SerializedName("imdb_rating")
    private Float imdbRating;
    private List<String> genres;


    public MovieWithBasicData(int apiId, String title, String posterLink, Integer year, String country, Float imdbRating, List<String> genres) {
        this.apiId = apiId;
        this.title = title;
        this.posterLink = posterLink;
        this.year = year;
        this.country = country;
        this.imdbRating = imdbRating;
        this.genres = genres;
    }

    public int getApiId() {
        return apiId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public Integer getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieWithBasicData: (" + title + ")";
    }
}
