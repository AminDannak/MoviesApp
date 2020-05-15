package ir.amindannak.movies.ui.activity.genres;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.amindannak.movies.R;
import ir.amindannak.movies.model.genre.Genre;

public class GenreAdapter extends ListAdapter<Genre, GenreAdapter.GenreVH> {

    private static final String TAG = GenreAdapter.class.getSimpleName();

    private static final DiffUtil.ItemCallback<Genre> GENRE_ITEM_CALLBACK = new DiffUtil.ItemCallback<Genre>() {
        @Override
        public boolean areItemsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.getApiId().equals(newItem.getApiId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return areItemsTheSame(oldItem, newItem);
        }
    };

    private OnGenreClickListener onGenreClickListener;

    GenreAdapter(OnGenreClickListener onGenreClickListener) {
        super(GENRE_ITEM_CALLBACK);
        this.onGenreClickListener = onGenreClickListener;
    }


    @NonNull
    @Override
    public GenreVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genres_list_item, parent, false);
        return new GenreVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreVH holder, int position) {
//        Genre genre = genreList.get(position);
        Genre genre = getItem(position);
        holder.itemView.setTag(genre);
        holder.bind();
    }

    class GenreVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView title;

        GenreVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_genres_list_item);
            title = itemView.findViewById(R.id.tv_genres_list_item);
        }

        void bind() {
            Genre genre = (Genre) itemView.getTag();
            Log.d(TAG, "bind: " + genre);
            image.setImageDrawable(genre.getImageDrawable());
            title.setText(genre.getDisplayedName());
            itemView.setOnClickListener(this);

            ViewCompat.setTransitionName(image, genre.getDisplayedName());
        }

        @Override
        public void onClick(View v) {
            onGenreClickListener.OnGenreItemClicked((Genre) itemView.getTag(), image);
        }
    }

    interface OnGenreClickListener {
        void OnGenreItemClicked(Genre genre, ImageView imageView);
    }

}
