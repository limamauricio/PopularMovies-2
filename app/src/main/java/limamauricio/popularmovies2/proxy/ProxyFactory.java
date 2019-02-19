package limamauricio.popularmovies2.proxy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProxyFactory {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static Retrofit getInstance(){

        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}
