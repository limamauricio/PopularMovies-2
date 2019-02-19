package limamauricio.popularmovies2.proxy;

import limamauricio.popularmovies2.model.MoviesRequestResponse;
import limamauricio.popularmovies2.model.ReviewsRequestResponse;
import limamauricio.popularmovies2.model.TrailersRequestResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Proxy {

    @GET("movie/popular")
    Call<MoviesRequestResponse> getPopularMovies(@Query("page") int page,
                                                 @Query("api_key") String api);


    @GET("movie/top_rated")
    Call<MoviesRequestResponse> getTopMovies(@Query("page") int page,
                                             @Query("api_key") String api);

    @GET("movie/{id}/videos")
    Call<TrailersRequestResponse> getTrailers(@Path("id") int id,
                                              @Query("api_key") String api);

    @GET("movie/{id}/reviews")
    Call<ReviewsRequestResponse> getReviews(@Path("id") int id,
                                            @Query("api_key") String api);

}
