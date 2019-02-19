package limamauricio.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class MoviesRequestResponse implements Serializable {

    @Getter
    @Setter
    @SerializedName("page")
    private int page;

    @Getter
    @Setter
    @SerializedName("total_results")
    private int totalMovies;

    @Getter
    @Setter
    @SerializedName("total_pages")
    private int totalPages;

    @Getter
    @Setter
    @SerializedName("results")
    private List<Movie> movieList;
}
