package limamauricio.popularmovies2.ui.details.review;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ReviewViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.review_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {

        Review review = this.reviewList.get(i);
        reviewViewHolder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
