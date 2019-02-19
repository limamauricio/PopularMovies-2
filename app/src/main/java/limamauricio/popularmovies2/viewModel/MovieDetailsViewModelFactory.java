package limamauricio.popularmovies2.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import limamauricio.popularmovies2.persistence.AppDatabase;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final Integer movieID;

    public MovieDetailsViewModelFactory(AppDatabase database, Integer movieID) {
        this.database = database;
        this.movieID = movieID;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(database, movieID);
    }
}
