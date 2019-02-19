package limamauricio.popularmovies2.ui.details.trailers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import limamauricio.popularmovies2.R;
import limamauricio.popularmovies2.model.Trailer;
import limamauricio.popularmovies2.util.OnClickListenerEvent;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private final List<Trailer> trailerList;
    private final OnClickListenerEvent onClickListenerEvent;

    public TrailerAdapter(List<Trailer> trailers, OnClickListenerEvent onClickListenerEvent) {
        this.trailerList = trailers;
        this.onClickListenerEvent = onClickListenerEvent;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrailerViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.trailer_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        Trailer trailer = this.trailerList.get(i);
        trailerViewHolder.bind(trailer, onClickListenerEvent);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
}
