package limamauricio.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Review implements Serializable {

    @Getter
    @Setter
    @SerializedName("author")
    private String author;

    @Getter
    @Setter
    @SerializedName("content")
    private String review;
}
