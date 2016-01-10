package be.dieterholvoet.beerguide.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BeerRating;
import be.dieterholvoet.beerguide.model.RatingElement;

/**
 * Created by Dieter on 30/12/2015.
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private List<RatingElement> ratingElements;
    private NewBeerActivity activity;

    public RatingAdapter(List<RatingElement> ratingElements, Activity activity) {
        this.ratingElements = ratingElements;
        this.activity = (NewBeerActivity) activity;
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
    public RatingAdapter.RatingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_rating_card, viewGroup, false);

        RatingAdapter.RatingViewHolder vh = new RatingViewHolder(v, new RatingAdapter.RatingViewHolder.ViewHolderRatingChanges() {
            @Override
            public void onAppearanceFoamRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setFoam(rating);
                beer.setRating(beer_rating);
                activity.setBeer(beer);
            }

            @Override
            public void onAppearanceColorRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setColor(rating);
                beer.setRating(beer_rating);
                activity.setBeer(beer);
            }

            @Override
            public void onAppearanceClearnessRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setClarity(rating);
                beer.setRating(beer_rating);
            }

            @Override
            public void onTasteSweetnessRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setSweetness(rating);
                beer.setRating(beer_rating);
            }

            @Override
            public void onTasteSournessRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setSourness(rating);
                beer.setRating(beer_rating);
            }

            @Override
            public void onTasteBitternessRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setBitterness(rating);
                beer.setRating(beer_rating);
            }

            @Override
            public void onTasteFullnessRatingChange(int rating) {
                Beer beer = activity.getBeer();
                BeerRating beer_rating = beer.getRating();
                beer_rating.setFullness(rating);
                beer.setRating(beer_rating);
            }
        });

        return vh;
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder implements RatingBar.OnRatingBarChangeListener {
        protected String name;
        protected TextView title;
        protected TextView description;
        protected ImageView banner;
        protected RatingBar rating;
        protected ViewHolderRatingChanges listener;

        public RatingViewHolder(View v, ViewHolderRatingChanges listener) {
            super(v);
            this.listener = listener;
            this.title =  (TextView) v.findViewById(R.id.rating_card_title);
            this.description = (TextView)  v.findViewById(R.id.rating_card_description);
            this.banner = (ImageView)  v.findViewById(R.id.item_beer_img);
            this.rating = (RatingBar) v.findViewById(R.id.rating_card_rating);
            this.rating.setOnRatingBarChangeListener(this);
            this.rating.setTag(this); // Source: http://stackoverflow.com/a/26942340/2637528
            this.name = this.title.getText().toString();
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            RatingViewHolder viewHolder = (RatingViewHolder) ratingBar.getTag();
            int rating_num = Math.round(ratingBar.getRating());
            
            if(rating != 0) {
                switch(viewHolder.name) {
                    case "Foam":
                        listener.onAppearanceFoamRatingChange(rating_num);
                        break;

                    case "Color":
                        listener.onAppearanceColorRatingChange(rating_num);
                        break;

                    case "Clearness":
                        listener.onAppearanceClearnessRatingChange(rating_num);
                        break;

                    case "Sweet":
                        listener.onTasteSweetnessRatingChange(rating_num);
                        break;

                    case "Sour":
                        listener.onTasteSournessRatingChange(rating_num);
                        break;

                    case "Bitter":
                        listener.onTasteBitternessRatingChange(rating_num);
                        break;

                    case "Full":
                        listener.onTasteFullnessRatingChange(rating_num);
                        break;

                    default:
                        break;
                }
            }
        }

        // Source: http://stackoverflow.com/a/24933117/2637528
        public static interface ViewHolderRatingChanges {
            public void onAppearanceFoamRatingChange(int rating);
            public void onAppearanceColorRatingChange(int rating);
            public void onAppearanceClearnessRatingChange(int rating);
            public void onTasteSweetnessRatingChange(int rating);
            public void onTasteSournessRatingChange(int rating);
            public void onTasteBitternessRatingChange(int rating);
            public void onTasteFullnessRatingChange(int rating);
        }
    }
}
