package limamauricio.popularmovies2.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import limamauricio.popularmovies2.model.Movie;
import limamauricio.popularmovies2.persistence.AppDatabase;
import lombok.Getter;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    @Getter
    private LiveData<List<Movie>> movies;

    public MainViewModel(Application application){
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "getting favorite movies");
        movies = database.movieDao().getAllMovies();

    }

}
