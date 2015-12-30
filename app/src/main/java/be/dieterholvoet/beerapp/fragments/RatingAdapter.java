package be.dieterholvoet.beerapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.model.RatingElement;

/**
 * Created by Dieter on 30/12/2015.
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private List<RatingElement> ratingElements;

    public RatingAdapter(List<RatingElement> ratingElements) {
        this.ratingElements = ratingElements;
    }

    @Override
    public int getItemCount() {
        return ratingElements.size();
    }

    @Override
    public void onBindViewHolder(RatingViewHolder ratingViewHolder, int i) {
        RatingElement r = ratingElements.get(i);
        ratingViewHolder.title.setText(r.getTitle());
        ratingViewHolder.description.setText(r.getDescription());
        ratingViewHolder.banner.setImageResource(r.getBanner());
        ratingViewHolder.rating.setRating(r.getRating());
    }

    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.rating_card_view, viewGroup, false);

        return new RatingViewHolder(itemView);
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView description;
        protected ImageView banner;
        protected RatingBar rating;

        public RatingViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.rating_card_title);
            description = (TextView)  v.findViewById(R.id.rating_card_description);
            banner = (ImageView)  v.findViewById(R.id.rating_card_banner);
            rating = (RatingBar) v.findViewById(R.id.rating_card_rating);
        }
    }
}
