package ir.amindannak.movies.model.genre;

import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.amindannak.movies.R;
import ir.amindannak.movies.util.App;

import static ir.amindannak.movies.model.genre.GenreEnum.ACTION;
import static ir.amindannak.movies.model.genre.GenreEnum.ADVENTURE;
import static ir.amindannak.movies.model.genre.GenreEnum.ANIMATION;
import static ir.amindannak.movies.model.genre.GenreEnum.BIOGRAPHY;
import static ir.amindannak.movies.model.genre.GenreEnum.COMEDY;
import static ir.amindannak.movies.model.genre.GenreEnum.CRIME;
import static ir.amindannak.movies.model.genre.GenreEnum.DRAMA;
import static ir.amindannak.movies.model.genre.GenreEnum.FAMILY;
import static ir.amindannak.movies.model.genre.GenreEnum.FANTASY;
import static ir.amindannak.movies.model.genre.GenreEnum.FILM_NOIR;
import static ir.amindannak.movies.model.genre.GenreEnum.HISTORY;
import static ir.amindannak.movies.model.genre.GenreEnum.HORROR;
import static ir.amindannak.movies.model.genre.GenreEnum.MUSIC;
import static ir.amindannak.movies.model.genre.GenreEnum.MUSICAL;
import static ir.amindannak.movies.model.genre.GenreEnum.MYSTERY;
import static ir.amindannak.movies.model.genre.GenreEnum.ROMANCE;
import static ir.amindannak.movies.model.genre.GenreEnum.SCI_FI;
import static ir.amindannak.movies.model.genre.GenreEnum.SPORT;
import static ir.amindannak.movies.model.genre.GenreEnum.THRILLER;
import static ir.amindannak.movies.model.genre.GenreEnum.WAR;
import static ir.amindannak.movies.model.genre.GenreEnum.WESTERN;


public class GenreUtils {

    private static final String TAG = GenreUtils.class.getSimpleName();

    private static final Map<GenreEnum, GenreResourceHolder> GENRE_RESOURCES_MAP = new HashMap<>();
    public static final Map<String, Integer> GENRE_API_NAME_ID_MAP = new HashMap<>();

    static {

        GENRE_RESOURCES_MAP.put(CRIME, getResourceHolder(
                R.string.genre_displayed_name_crime,
                R.drawable.crime,
                R.integer.genre_api_id_crime));
        GENRE_RESOURCES_MAP.put(DRAMA, getResourceHolder(
                R.string.genre_displayed_name_drama,
                R.drawable.drama,
                R.integer.genre_api_id_drama));
        GENRE_RESOURCES_MAP.put(ACTION, getResourceHolder(
                R.string.genre_displayed_name_action,
                R.drawable.action,
                R.integer.genre_api_id_action));
        GENRE_RESOURCES_MAP.put(BIOGRAPHY, getResourceHolder(
                R.string.genre_displayed_name_biography,
                R.drawable.biography,
                R.integer.genre_api_id_biography));
        GENRE_RESOURCES_MAP.put(HISTORY, getResourceHolder(
                R.string.genre_displayed_name_history,
                R.drawable.history,
                R.integer.genre_api_id_history));
        GENRE_RESOURCES_MAP.put(ADVENTURE, getResourceHolder(
                R.string.genre_displayed_name_adventure,
                R.drawable.adventure,
                R.integer.genre_api_id_adventure));
        GENRE_RESOURCES_MAP.put(FANTASY, getResourceHolder(
                R.string.genre_displayed_name_fantasy,
                R.drawable.fantasy,
                R.integer.genre_api_id_fantasy));
        GENRE_RESOURCES_MAP.put(WESTERN, getResourceHolder(
                R.string.genre_displayed_name_western,
                R.drawable.western,
                R.integer.genre_api_id_western));
        GENRE_RESOURCES_MAP.put(COMEDY, getResourceHolder(
                R.string.genre_displayed_name_comedy,
                R.drawable.comedy,
                R.integer.genre_api_id_comedy));
        GENRE_RESOURCES_MAP.put(SCI_FI, getResourceHolder(
                R.string.genre_displayed_name_sci_fi,
                R.drawable.sci_fi,
                R.integer.genre_api_id_sci_fi));
        GENRE_RESOURCES_MAP.put(MYSTERY, getResourceHolder(
                R.string.genre_displayed_name_mystery,
                R.drawable.mystery,
                R.integer.genre_api_id_mystery));
        GENRE_RESOURCES_MAP.put(THRILLER, getResourceHolder(
                R.string.genre_displayed_name_thriller,
                R.drawable.thriller,
                R.integer.genre_api_id_thriller));
        GENRE_RESOURCES_MAP.put(FAMILY, getResourceHolder(
                R.string.genre_displayed_name_family,
                R.drawable.family,
                R.integer.genre_api_id_family));
        GENRE_RESOURCES_MAP.put(WAR, getResourceHolder(
                R.string.genre_displayed_name_war,
                R.drawable.war,
                R.integer.genre_api_id_war));
        GENRE_RESOURCES_MAP.put(ANIMATION, getResourceHolder(
                R.string.genre_displayed_name_animation,
                R.drawable.animation,
                R.integer.genre_api_id_animation));
        GENRE_RESOURCES_MAP.put(ROMANCE, getResourceHolder(
                R.string.genre_displayed_name_romance,
                R.drawable.romance,
                R.integer.genre_api_id_romance));
        GENRE_RESOURCES_MAP.put(HORROR, getResourceHolder(
                R.string.genre_displayed_name_horror,
                R.drawable.horror,
                R.integer.genre_api_id_horror));
        GENRE_RESOURCES_MAP.put(MUSIC, getResourceHolder(
                R.string.genre_displayed_name_music,
                R.drawable.music,
                R.integer.genre_api_id_music));
        GENRE_RESOURCES_MAP.put(FILM_NOIR, getResourceHolder(
                R.string.genre_displayed_name_film_noir,
                R.drawable.film_noir,
                R.integer.genre_api_id_film_noir));
        GENRE_RESOURCES_MAP.put(MUSICAL, getResourceHolder(
                R.string.genre_displayed_name_musical,
                R.drawable.musical,
                R.integer.genre_api_id_musical));
        GENRE_RESOURCES_MAP.put(SPORT, getResourceHolder(
                R.string.genre_displayed_name_sport,
                R.drawable.sport,
                R.integer.genre_api_id_sport));

        // don't move this above the 'put's. it's dependent on them
        for (Genre genre : getAllGenres()) {
            GENRE_API_NAME_ID_MAP.put(genre.getDisplayedName(), genre.getApiId());
        }
    }

    public static List<Genre> getAllGenres() {
        List<Genre> list = new ArrayList<>();
        for (GenreEnum genreEnum : GenreEnum.values()) {
            list.add(new Genre(genreEnum));
            Log.d(TAG, "getAllGenres: added " + GenreEnum.enumToString(genreEnum));
        }
        return list;
    }


    static GenreResourceHolder getResourceHolder(GenreEnum genreEnum) {
        return GENRE_RESOURCES_MAP.get(genreEnum);
    }

    private static GenreResourceHolder getResourceHolder(@StringRes int name, @DrawableRes int image, @IntegerRes int id) {
        String displayedNameResName = App.getRes().getResourceEntryName(name);
        String imageResName = App.getRes().getResourceEntryName(image);
        int apiId = App.getRes().getInteger(id);
        return new GenreResourceHolder(displayedNameResName, imageResName, apiId);
    }

    static class GenreResourceHolder {
        private String displayedNameResName;
        private String imageResName;
        private int apiId;

        GenreResourceHolder(String displayedNameResName, String imageResName, int apiId) {
            this.displayedNameResName = displayedNameResName;
            this.imageResName = imageResName;
            this.apiId = apiId;
        }

        String getDisplayedNameResName() {
            return displayedNameResName;
        }

        String getImageResName() {
            return imageResName;
        }

        int getApiId() {
            return apiId;
        }

    }

}
