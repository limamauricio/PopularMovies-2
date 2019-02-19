package limamauricio.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class TrailersRequestResponse implements Serializable {

    @Getter
    @Setter
    @SerializedName("id")
    private int id;

    @Getter
    @Setter
    @SerializedName("results")
    private List<Trailer> trailerList;
}
