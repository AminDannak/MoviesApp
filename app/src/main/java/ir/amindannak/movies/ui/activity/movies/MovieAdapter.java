package ir.amindannak.movies.ui.activity.movies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ir.amindannak.movies.R;
import ir.amindannak.movies.model.movie.MovieWithBasicData;
import ir.amindannak.movies.ui.shared.Animations;
import ir.amindannak.movies.ui.shared.Utils;
import ir.amindannak.movies.util.App;

public class MovieAdapter extends PagedListAdapter<MovieWithBasicData, MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final DiffUtil.ItemCallback<MovieWithBasicData> MOVIE_ITEM_CALLBACK = new DiffUtil.ItemCallback<MovieWithBasicData>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieWithBasicData oldItem, @NonNull MovieWithBasicData newItem) {
            return oldItem.getApiId() == newItem.getApiId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieWithBasicData oldItem, @NonNull MovieWithBasicData newItem) {
            return areItemsTheSame(oldItem, newItem);
        }
    };

    private final OnMovieClickListener onMovieClickListener;
    private final RequestManager glideRequestManager;

    MovieAdapter(OnMovieClickListener onMovieClickListener) {
        super(MOVIE_ITEM_CALLBACK);
        this.onMovieClickListener = onMovieClickListener;
        glideRequestManager = Glide.with(App.getApp());
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieWithBasicData movie = getItem(position);
        holder.itemView.setTag(movie);
        holder.bind();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView movieTitle;
        private ImageView poster;
        private TextView releaseYear;
        private TextView imdbRating;
        private TextView genres;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.tv_movie_title);
            poster = itemView.findViewById(R.id.iv_movie_poster);
            releaseYear = itemView.findViewById(R.id.tv_release_year);
            imdbRating = itemView.findViewById(R.id.tv_imdb_rating);
//            country = itemView.findViewById(R.id.tv_country);
            genres = itemView.findViewById(R.id.tv_genres);
        }

        private void bind() {
            MovieWithBasicData movie = (MovieWithBasicData) itemView.getTag();
            movieTitle.setText(movie.getTitle());
            releaseYear.setText(Utils.getString(movie.getYear()));
            imdbRating.setText(Utils.getString(movie.getImdbRating()));
            String genresStr = movie.getGenres().toString();
            genres.setText(genresStr.substring(1, genresStr.length() - 1));
            setImage(movie.getPosterLink(), poster);

            itemView.setOnClickListener(this);
        }

        private void setImage(String url, ImageView poster) {
            Log.d(TAG, "setImage: downloading " + url);
            glideRequestManager
                    .load(url)
                    .placeholder(R.drawable.svg_film_reel)
                    .error(R.drawable.svg_do_not_record_warning)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (isFirstResource) {
                                ObjectAnimator animator = Animations.fadeOut(poster);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        poster.setScaleType(ImageView.ScaleType.FIT_XY);
                                        poster.setPadding(0, 0, 0, 0);
                                        poster.setImageDrawable(resource);
                                        Animations.fadeIn(poster).start();
                                    }
                                });
                                animator.start();
                            } else {
                                poster.setImageDrawable(resource);
                            }
                            return true;
                        }
                    })
                    .into(poster);
        }

        @Override
        public void onClick(View v) {
            MovieWithBasicData movie = (MovieWithBasicData) itemView.getTag();
            onMovieClickListener.onMovieClicked(movie);
        }
    }

    interface OnMovieClickListener {
        void onMovieClicked(MovieWithBasicData movie);
    }

}
