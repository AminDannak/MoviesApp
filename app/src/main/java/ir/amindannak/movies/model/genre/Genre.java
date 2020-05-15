package ir.amindannak.movies.model.genre;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static ir.amindannak.movies.model.ResourceUtils.getDrawableFromResource;
import static ir.amindannak.movies.model.ResourceUtils.getStringFromResource;
import static ir.amindannak.movies.model.genre.GenreUtils.GenreResourceHolder;
import static ir.amindannak.movies.model.genre.GenreUtils.getResourceHolder;

@Entity(tableName = "genres")
public class Genre implements Serializable {

    private static final long serialVersionUID = 9_999_990L;

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer apiId;
    @ColumnInfo(name = "genre_name")
    private GenreEnum genreEnum;
    private String displayedNameResourceName;
    private String drawableResourceName;

    @Ignore
    Genre(GenreEnum genreEnum) {
        this.genreEnum = genreEnum;
        GenreResourceHolder holder = getResourceHolder(genreEnum);
        this.displayedNameResourceName = holder.getDisplayedNameResName();
        this.drawableResourceName = holder.getImageResName();
        this.apiId = holder.getApiId();
    }

    public Genre(GenreEnum genreEnum, String displayedNameResourceName, String drawableResourceName, int apiId) {
        this.genreEnum = genreEnum;
        this.displayedNameResourceName = displayedNameResourceName;
        this.drawableResourceName = drawableResourceName;
        this.apiId = apiId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public String getDisplayedNameResourceName() {
        return displayedNameResourceName;
    }

    public String getDrawableResourceName() {
        return drawableResourceName;
    }

    public String getDisplayedName() {
        return getStringFromResource(displayedNameResourceName);
    }

    public Drawable getImageDrawable() {
        return getDrawableFromResource(drawableResourceName);
    }


    public GenreEnum getGenreEnum() {
        return genreEnum;
    }


    @NonNull
    @Override
    public String toString() {
        return genreEnum.toString();
    }
}
