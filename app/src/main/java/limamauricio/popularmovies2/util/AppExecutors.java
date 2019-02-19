package limamauricio.popularmovies2.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.Getter;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors appExecutorsInstance;

    @Getter
    private final Executor diskIO;

    public AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance(){

        if (appExecutorsInstance == null){

            synchronized (LOCK){

                appExecutorsInstance = new AppExecutors(Executors.newSingleThreadExecutor());

            }

        }

        return appExecutorsInstance;
    }


}
