package ir.amindannak.movies.api;

import ir.amindannak.movies.model.movie.MovieWithAllData;
import ir.amindannak.movies.model.movie.MoviesApiCallResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    String BASE_URL = "http://moviesapi.ir/api/v1/";

    @GET("genres/{genre_id}/movies")
    Call<MoviesApiCallResponse> getMoviesByGenre(@Path("genre_id") int id, @Query("page") int page);

    @GET("movies/{id}")
    Call<MovieWithAllData> getMovieById(@Path("id") int id);

}
