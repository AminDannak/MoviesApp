package ir.amindannak.movies.ui.activity.genres;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.amindannak.movies.util.App;

// todo: add modes (uniform-spacing, with-margin, etc.)
// todo: handle horizontal grids too
public class VerticalGridItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private Integer horizontalSpacing;
    private Integer verticalSpacing;


    VerticalGridItemDecoration(int spanCount, @DimenRes int horizontalSpacing) {
        this.spanCount = spanCount;
        this.horizontalSpacing = App.getRes().getDimensionPixelSize(horizontalSpacing);
    }

    public VerticalGridItemDecoration(int spanCount, @DimenRes int horizontalSpacing, @DimenRes int verticalSpacing) {
        this(spanCount, horizontalSpacing);
        this.verticalSpacing = App.getRes().getDimensionPixelSize(verticalSpacing);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        boolean itemOnFirstColumn = column == 0;
        boolean itemOnLastColumn = column == (spanCount - 1);
        boolean itemOnTopRow = position < spanCount;

        int appliedVerticalSpacing = verticalSpacing != null ? verticalSpacing : horizontalSpacing;

        if (itemOnTopRow) {
            outRect.top = appliedVerticalSpacing;
            outRect.bottom = appliedVerticalSpacing;
        } else {
            outRect.top = 0;
            outRect.bottom = appliedVerticalSpacing;
        }

        if (itemOnFirstColumn) {
            outRect.left = horizontalSpacing;
        } else {
            outRect.left = horizontalSpacing / 2;
        }

        if (itemOnLastColumn) {
            outRect.right = horizontalSpacing;
        } else {
            outRect.right = horizontalSpacing / 2;
        }


    }
}
