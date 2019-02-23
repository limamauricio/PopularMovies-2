package limamauricio.popularmovies2.ui.movie;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private ItemClickListener mItemClickListener;
    private List<Movie> movieList;
    private Context mContext;

    public MovieAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new MovieViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_item,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void setMovies(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(Movie movie);
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_movie)
        ImageView imgMovie;

        @BindView(R.id.movie_item)
        CardView movieItem;

        private final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

        public MovieViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(final Movie movie){

            int screenFactor = 2;
            if(itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                screenFactor = 4;
            }

            movieItem.setLayoutParams(new ViewGroup.LayoutParams(
                    getScreenWidth()/screenFactor,
                    getMeasuredPosterHeight(getScreenWidth()/screenFactor)));
            Glide.with(itemView)
                    .load(IMAGE_URL +movie.getImgPath())
                    .into(imgMovie);

        }

        @Override
        public void onClick(View view) {
            Movie movie = new Movie();
            movie.setId(movieList.get(getAdapterPosition()).getId());
            movie.setImgPath(movieList.get(getAdapterPosition()).getImgPath());
            movie.setOverview(movieList.get(getAdapterPosition()).getOverview());
            movie.setReleaseDate(movieList.get(getAdapterPosition()).getReleaseDate());
            movie.setTitle(movieList.get(getAdapterPosition()).getTitle());
            movie.setVoteAverage(movieList.get(getAdapterPosition()).getVoteAverage());
            mItemClickListener.onItemClickListener(movie);
        }


        private int getScreenWidth() {
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        private int getMeasuredPosterHeight(int width) {
            return (int) (width * 1.5f);
        }
    }
}
