package limamauricio.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Trailer implements Serializable {


    @Getter
    @Setter
    @SerializedName("id")
    private String id;

    @Getter
    @Setter
    @SerializedName("key")
    private String key;

    @Getter
    @Setter
    @SerializedName("name")
    private String name;

}
