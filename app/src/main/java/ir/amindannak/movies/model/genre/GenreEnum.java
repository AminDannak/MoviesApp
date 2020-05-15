package ir.amindannak.movies.model.genre;

import androidx.room.TypeConverter;

public enum GenreEnum {

    CRIME,
    DRAMA,
    ACTION,
    BIOGRAPHY,
    HISTORY,
    ADVENTURE,
    FANTASY,
    WESTERN,
    COMEDY,
    SCI_FI,
    MYSTERY,
    THRILLER,
    FAMILY,
    WAR,
    ANIMATION,
    ROMANCE,
    HORROR,
    MUSIC,
    FILM_NOIR,
    MUSICAL,
    SPORT;

    @TypeConverter
    public static String enumToString(GenreEnum genreEnum) {
        return genreEnum.name();
    }

    @TypeConverter
    public static GenreEnum enumFromString(String string) {
        GenreEnum genreEnum = null;
        for (GenreEnum genre : values()) {
            if (genre.name().equals(string)) {
                genreEnum = genre;
            }
        }
        return genreEnum;
    }
}
