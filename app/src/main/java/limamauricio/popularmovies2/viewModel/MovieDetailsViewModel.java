package limamauricio.popularmovies2.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import limamauricio.popularmovies2.model.Movie;
import limamauricio.popularmovies2.persistence.AppDatabase;
import lombok.Getter;

public class MovieDetailsViewModel extends ViewModel {

    @Getter
    private LiveData<Movie> movie;

    public MovieDetailsViewModel(AppDatabase database, Integer movieId){

        movie = database.movieDao().getMovieById(movieId);

    }

}
