package ir.amindannak.movies.model.movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "metadata")
public class MoviesByGenreMetadata {

    @PrimaryKey
    private Integer genreId;

    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("per_page")
    private int perPage;
    @SerializedName("page_count")
    private int pageCount;
    @SerializedName("total_count")
    private int totalCount;

    public MoviesByGenreMetadata(int currentPage, int pageCount, int totalCount, int perPage) {
        this.currentPage = currentPage;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
        this.perPage = perPage;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPerPage() {
        return perPage;
    }

    @NonNull
    @Override
    public String toString() {
        return "MoviesByGenreMetadata{" +
                "genreId=" + genreId +
                ", currentPage=" + currentPage +
                ", perPage=" + perPage +
                ", pageCount=" + pageCount +
                ", totalCount=" + totalCount +
                '}';
    }

    public static boolean isCompletelyLoaded(@Nullable MoviesByGenreMetadata metadata) {
        if (metadata == null) return false;
        return metadata.getCurrentPage() == metadata.getPageCount();
    }

}