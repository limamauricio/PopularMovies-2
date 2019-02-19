package limamauricio.popularmovies2.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import limamauricio.popularmovies2.model.Movie;

@Dao
public interface MovieDao {

    @Query("select * from movie")
    LiveData<List<Movie>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovieFavorite(Movie movie);

    @Delete
    void deleteMovieFavorite(Movie movie);

    @Query("select * from movie where id = :movieId")
    LiveData<Movie> getMovieById(Integer movieId);

}
