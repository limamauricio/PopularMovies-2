package limamauricio.popularmovies2.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import limamauricio.popularmovies2.BuildConfig;
import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Movie;
import limamauricio.popularmovies2.model.Review;
import limamauricio.popularmovies2.model.ReviewsRequestResponse;
import limamauricio.popularmovies2.model.Trailer;
import limamauricio.popularmovies2.model.TrailersRequestResponse;
import limamauricio.popularmovies2.persistence.AppDatabase;
import limamauricio.popularmovies2.proxy.Proxy;
import limamauricio.popularmovies2.proxy.ProxyFactory;
import limamauricio.popularmovies2.ui.details.review.ReviewAdapter;
import limamauricio.popularmovies2.ui.details.trailers.TrailerAdapter;
import limamauricio.popularmovies2.util.AppExecutors;
import limamauricio.popularmovies2.util.OnClickListenerEvent;
import limamauricio.popularmovies2.viewModel.MovieDetailsViewModel;
import limamauricio.popularmovies2.viewModel.MovieDetailsViewModelFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.movieDetailsTitle)
    TextView movieDetailsTitle;

    @BindView(R.id.movieDetailsImage)
    ImageView movieDetailsPoster;

    @BindView(R.id.movieDetailsOverview)
    TextView movieDetailsOverview;

    @BindView(R.id.movieDetailsReleaseDate)
    TextView releaseDate;

    @BindView(R.id.movieDetailsRating)
    RatingBar ratingBar;

    @BindView(R.id.trailerRecyclerView)
    RecyclerView trailerRecyclerView;

    @BindView(R.id.reviewRecyclerView)
    RecyclerView reviewRecyclerView;

    private List<Trailer> trailerList;
    private List<Review> reviewList;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private Movie movie;
    private Menu menu;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        database = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        movie = (Movie) bundle.getSerializable("movie");

        checkMovieFavorite(movie);


    }

    private void checkMovieFavorite(final Movie movie) {

        MovieDetailsViewModelFactory factory
                = new MovieDetailsViewModelFactory(database,movie.getId());

        final MovieDetailsViewModel viewModel
                = ViewModelProviders.of(this,factory).get(MovieDetailsViewModel.class);

        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie m) {
                if (m == null){
                    movie.setFavorite(false);
                    prepareDetailsLayout(movie);
                }else {
                    movie.setFavorite(true);
                    prepareDetailsLayout(movie);
                }
            }
        });

    }


    private void prepareDetailsLayout(Movie movieDetail){

        String IMAGE_URL = "http://image.tmdb.org/t/p/w780";
        Glide.with(this).load(IMAGE_URL +movieDetail.getImgPath()).into(movieDetailsPoster);
        movieDetailsTitle.setText(movieDetail.getTitle());
        movieDetailsOverview.setText(movieDetail.getOverview());
        releaseDate.setText(movieDetail.getReleaseDate());
        ratingBar.setRating(movieDetail.getVoteAverage());
        prepareTrailerLayout();
        prepareReviewLayout();
        getReviewById(movieDetail.getId());
        getTrailerById(movieDetail.getId());

    }

    private void prepareTrailerLayout(){

        trailerRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, true));

    }

    private void prepareReviewLayout(){

        reviewRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));

    }

    private void getTrailerById(int movieId){

        Proxy proxy = ProxyFactory.getInstance().create(Proxy.class);

        if (isConnectedToInternet(getApplicationContext())){

            Call<TrailersRequestResponse> call = proxy.getTrailers(movieId, BuildConfig.ApiKey);

            call.enqueue(new Callback<TrailersRequestResponse>() {
                @Override
                public void onResponse(Call<TrailersRequestResponse> call, Response<TrailersRequestResponse> response) {

                    trailerList = response.body().getTrailerList();
                    trailerAdapter = new TrailerAdapter(trailerList, new OnClickListenerEvent() {

                        @Override
                        public void onTrailerClick(Trailer trailer) {

                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(YOUTUBE_URL + trailer.getKey()));

                            startActivity(intent);
                        }
                    });
                    trailerRecyclerView.setAdapter(trailerAdapter);


                }

                @Override
                public void onFailure(Call<TrailersRequestResponse> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, getString(R.string.failed_to_get_trailers), Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }


    private void getReviewById(int movieId){

        Proxy proxy = ProxyFactory.getInstance().create(Proxy.class);

        if (isConnectedToInternet(getApplicationContext())){

            Call<ReviewsRequestResponse> call = proxy.getReviews(movieId, BuildConfig.ApiKey);

            call.enqueue(new Callback<ReviewsRequestResponse>() {
                @Override
                public void onResponse(Call<ReviewsRequestResponse> call, Response<ReviewsRequestResponse> response) {

                    reviewList = response.body().getReviewList();
                    reviewAdapter = new ReviewAdapter(reviewList);
                    reviewRecyclerView.setAdapter(reviewAdapter);
                }

                @Override
                public void onFailure(Call<ReviewsRequestResponse> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, getString(R.string.failed_to_get_trailers), Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = cm.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public void onFavoriteButtonClicked(){

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (!checkBoolean(movie.isFavorite())){

                    database.movieDao().addMovieFavorite(movie);
                    movie.setFavorite(true);


                }else {
                    database.movieDao().deleteMovieFavorite(movie);
                    movie.setFavorite(false);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        if (movie.isFavorite())
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        else
            menu.getItem(0).setIcon(R.drawable.ic_unfavorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_favorite) {
            if (!movie.isFavorite())
                item.setIcon(R.drawable.ic_favorite);
            else
                item.setIcon(R.drawable.ic_unfavorite);
            onFavoriteButtonClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean checkBoolean(Boolean b){
        return b == null ? false : b;
    }
}
