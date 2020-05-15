package ir.amindannak.movies.ui.activity.genres;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.amindannak.movies.R;
import ir.amindannak.movies.model.genre.Genre;
import ir.amindannak.movies.ui.activity.movies.MoviesActivity;
import ir.amindannak.movies.ui.viewmodel.GenresVM;


public class GenresActivity extends AppCompatActivity implements GenreAdapter.OnGenreClickListener {

    private static final String TAG = GenresActivity.class.getSimpleName();

    public static final String EXTRA_KEY_GENRE = "genre to be sent to movies activity";

    private GenresVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        viewModel = new ViewModelProvider(this).get(GenresVM.class);
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        GenreAdapter adapter = new GenreAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.rv_genres_act);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalGridItemDecoration(3, R.dimen.genre_grid_horizontal_spacing));
        recyclerView.setAdapter(adapter);
        viewModel.getAllGenres().observe(this, adapter::submitList);
    }

    @Override
    public void OnGenreItemClicked(Genre genre, ImageView imageView) {
        Intent intent = new Intent(this, MoviesActivity.class);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this, imageView, getString(R.string.movies_act_genre_image_transition_name)
//        );
        intent.putExtra(EXTRA_KEY_GENRE, genre);
//        startActivity(intent, options.toBundle());
        startActivity(intent);
    }
}
