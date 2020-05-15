package ir.amindannak.movies.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesApiCallResponse {

    @SerializedName("data")
    private List<MovieWithBasicData> movies;

    @SerializedName("metadata")
    private MoviesByGenreMetadata metadata;

    public List<MovieWithBasicData> getMovies() {
        return movies;
    }

    public MoviesByGenreMetadata getMetadata() {
        return metadata;
    }

}
