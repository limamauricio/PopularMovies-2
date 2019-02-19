package limamauricio.popularmovies2.ui.details.trailers;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Trailer;
import limamauricio.popularmovies2.util.OnClickListenerEvent;

public class TrailerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_trailer)
    ImageView trailerImage;

    @BindView(R.id.trailerName)
    TextView trailerName;

    @BindView(R.id.trailer_item)
    CardView trailerItem;

    private final String TRAILER_IMG = "http://img.youtube.com/vi/";
    private final String IMG_TRAILER_FORMAT = "/0.jpg";

    public TrailerViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(final Trailer trailer, final OnClickListenerEvent onClickListenerEvent){

        Glide.with(itemView)
                .load(TRAILER_IMG + trailer.getKey() + IMG_TRAILER_FORMAT)
                .into(trailerImage);

        trailerName.setText(trailer.getName());

        trailerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerEvent.onTrailerClick(trailer);
            }

        });
    }
}
