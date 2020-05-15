package ir.amindannak.movies.model.movie;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movie")
public class MovieWithAllData {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int apiId;
    private String title;
    @SerializedName("poster")
    private String posterLink;
    private String year;
    private String country;
    @SerializedName("imdb_rating")
    private String imdbRating;
    private List<String> genres;
    @SerializedName("images")
    private List<String> imagesLinks;
    private String released;
    private String runtime;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String awards;
    @SerializedName("imdb_votes")
    private String imdbVotes;

    public MovieWithAllData(int apiId,
                            String title,
                            String posterLink,
                            String year,
                            String country,
                            String imdbRating,
                            List<String> genres,
                            List<String> imagesLinks,
                            String released,
                            String runtime,
                            String director,
                            String writer,
                            String actors,
                            String plot,
                            String awards,
                            String imdbVotes) {
        this.apiId = apiId;
        this.title = title;
        this.posterLink = posterLink;
        this.year = year;
        this.country = country;
        this.imdbRating = imdbRating;
        this.genres = genres;
        this.imagesLinks = imagesLinks;
        this.released = released;
        this.runtime = runtime;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.awards = awards;
        this.imdbVotes = imdbVotes;
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

    public String getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getImagesLinks() {
        return imagesLinks;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getAwards() {
        return awards;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieWithAllData{" +
                "title='" + title + '\'' +
                '}';
    }
}
