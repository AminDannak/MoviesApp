package ir.amindannak.movies.ui.activity.movie_details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.Objects;

import ir.amindannak.movies.R;
import ir.amindannak.movies.ui.shared.Animations;
import ir.amindannak.movies.util.App;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ScreenshotsVH> {

    private static final String TAG = ScreenshotAdapter.class.getSimpleName();

    private List<String> urls;

    ScreenshotAdapter(List<String> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public ScreenshotsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_screenshots_movie_details_act, parent, false);
        return new ScreenshotsVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenshotsVH holder, int position) {
        holder.bind(urls.get(position));
    }

    @Override
    public int getItemCount() {
        return urls == null ? 0 : urls.size();
    }

    class ScreenshotsVH extends RecyclerView.ViewHolder {
        private ImageView screenshot;


        ScreenshotsVH(@NonNull View itemView) {
            super(itemView);
            screenshot = itemView.findViewById(R.id.iv_movie_screenshot);
            itemView.setTag(Boolean.FALSE);
            Log.d(TAG, "ScreenshotsVH: item view tag set to: " + itemView.getTag());
        }

        void bind(String url) {
//            Log.d(TAG, "bind: called");
            Glide.with(App.getApp())
                    .load(url)
                    .placeholder(R.drawable.svg_film_reel)
                    .error(R.drawable.svg_do_not_record_warning)
                    .transition(DrawableTransitionOptions.withCrossFade(700))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d(TAG, "onLoadFailed: msg: " + Objects.requireNonNull(e).getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ObjectAnimator animator = Animations.fadeOut(screenshot);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    screenshot.setPadding(0, 0, 0, 0);
                                    screenshot.setImageDrawable(resource);
                                    Animations.fadeIn(screenshot).start();
                                }
                            });
                            animator.start();
                            return true;
                        }
                    })
                    .into(screenshot);
        }
    }
}
