package limamauricio.popularmovies2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "movie")
public class Movie implements Serializable {

    @Getter
    @Setter
    @SerializedName("id")
    @PrimaryKey
    private int id;

    @Getter
    @Setter
    @SerializedName("title")
    private String title;

    @Getter
    @Setter
    @SerializedName("poster_path")
    private String imgPath;

    @Getter
    @Setter
    @SerializedName("overview")
    private String overview;

    @Getter
    @Setter
    @SerializedName("vote_average")
    private float voteAverage;

    @Getter
    @Setter
    @SerializedName("release_date")
    private String releaseDate;

    @Getter
    @Setter
    @Ignore
    private boolean favorite;

}
