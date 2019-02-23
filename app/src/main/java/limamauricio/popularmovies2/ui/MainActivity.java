package limamauricio.popularmovies2.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import limamauricio.popularmovies2.BuildConfig;
import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Movie;
import limamauricio.popularmovies2.model.MoviesRequestResponse;
import limamauricio.popularmovies2.proxy.Proxy;
import limamauricio.popularmovies2.proxy.ProxyFactory;
import limamauricio.popularmovies2.ui.details.MovieDetailsActivity;
import limamauricio.popularmovies2.ui.movie.MovieAdapter;
import limamauricio.popularmovies2.util.EndlessRecyclerViewScrollListener;
import limamauricio.popularmovies2.viewModel.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener{


    private static final String API_KEY = BuildConfig.ApiKey;
    private static int totalPages;
    private static int sort = 1;
    private Call<MoviesRequestResponse> call;
    private List<Movie> movieList;
    private MovieAdapter movieAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_TAG = "Movie";

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {

        GridLayoutManager manager = new GridLayoutManager(this,2);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new GridLayoutManager(this,4);
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            manager = new GridLayoutManager(this,2);
        }

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return 1;
            }
        });

        movieAdapter = new MovieAdapter(this,this);
        movieRecyclerView.setLayoutManager(manager);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if ((page + 1) <= totalPages) {
                    loadPage(page + 1);
                }
            }
        };

        movieRecyclerView.addOnScrollListener(scrollListener);
        movieRecyclerView.setAdapter(movieAdapter);

        checkNetworkConnection(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                sort = 1;
                loadPage(1);
                break;
            case R.id.top_rated:
                sort = 2;
                loadPage(1);
                break;
            case R.id.favorites_list:
                sort = 3;
                prepareViewModel();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void checkNetworkConnection(Context context){

        if(!isConnectedToInternet(context)){
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, getString(R.string.loading_movies), Toast.LENGTH_SHORT).show();
            loadPage(1);
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

    private void loadPage(final int page) {
        Proxy proxy = ProxyFactory.getInstance().create(Proxy.class);

        if (isConnectedToInternet(getApplicationContext())){
            switch(sort){
                case 1:
                    call = proxy.getPopularMovies(page, API_KEY);
                    loadMoviesFromServer(page);
                    break;
                case 2:
                    call = proxy.getTopMovies(page, API_KEY);
                    loadMoviesFromServer(page);
                    break;
                case 3:
                    //prepareViewModel();
                    break;
            }

        }else {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void loadMoviesFromServer(final int page){

        call.enqueue(new Callback<MoviesRequestResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesRequestResponse> call, @NonNull Response<MoviesRequestResponse> response) {

                if(page == 1) {
                    movieList = response.body().getMovieList();
                    totalPages = response.body().getTotalPages();
                    movieAdapter.setMovies(movieList);
                } else {
                    List<Movie> movies = response.body().getMovieList();
                    for(Movie movie : movies){
                        movieList.add(movie);
                        movieAdapter.notifyItemInserted(movieList.size() - 1);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<MoviesRequestResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.failed_to_get_movies), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClickListener(Movie movie) {

        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movie);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void prepareViewModel(){

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "display favorite movies");
                if (movieList == null)
                    movieList = new LinkedList<>();
                movieList.clear();
                movieList.addAll(movies);
                movieAdapter.setMovies(movieList);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<Movie> movies = movieList;
        outState.putSerializable(MOVIE_TAG, (Serializable) movies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieList = (List<Movie>) savedInstanceState.getSerializable(MOVIE_TAG);
        movieAdapter.setMovies(movieList);


    }
}
