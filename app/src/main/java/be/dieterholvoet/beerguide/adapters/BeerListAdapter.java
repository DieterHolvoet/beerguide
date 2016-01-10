package be.dieterholvoet.beerguide.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import java.util.List;

import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBCategory;

/**
 * Created by Dieter on 8/01/2016.
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerListViewHolder> {

    private List<Beer> beers;
    private Activity activity;

    public BeerListAdapter(List<Beer> beers, Activity activity) {
        this.beers = beers;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    @Override
    public void onBindViewHolder(BeerListViewHolder ratingViewHolder, int i) {
        Beer beer = beers.get(i);
        BreweryDBBeer bdb;
        BreweryDBCategory category;

        if(beer.getBdb() == null) {
            Log.e("BEER", "BreweryDBBeer is null");
            bdb = new BreweryDBBeer();

        } else {
            bdb = beer.getBdb();
        }

        if(bdb.getStyle() == null) {
            category = new BreweryDBCategory();

        } else {
            category = bdb.getStyle().getCategory();
        }

        String name = bdb.getName();
        String cat = (category.getName() == null || "".equals(category.getName())) ? "unknown" : category.getName();
        float rating = beer.getRating().getRating();

        ratingViewHolder.name.setText(name);
        ratingViewHolder.category.setText(cat);
        ratingViewHolder.rating.setRating(rating);
        // ratingViewHolder.img.setImageResource();

        Drawable starDrawable = ContextCompat.getDrawable(activity, R.drawable.star_full);
        LayoutParams params = (LayoutParams) ratingViewHolder.rating.getLayoutParams();
        params.height = starDrawable.getMinimumHeight();
        ratingViewHolder.rating.setLayoutParams(params);
    }

    @Override
    public BeerListAdapter.BeerListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_beer, viewGroup, false);

        BeerListAdapter.BeerListViewHolder vh = new BeerListViewHolder(v, new BeerListAdapter.BeerListViewHolder.ViewHolderBeerClick() {

        });

        return vh;
    }

    public static class BeerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ViewHolderBeerClick listener;
        protected TextView name;
        protected TextView category;
        protected ImageView img;
        protected RatingBar rating;

        public BeerListViewHolder(View v, ViewHolderBeerClick listener) {
            super(v);
            this.listener = listener;
            this.name =  (TextView) v.findViewById(R.id.item_beer_name);
            this.category = (TextView)  v.findViewById(R.id.item_beer_category_text);
            this.rating = (RatingBar)  v.findViewById(R.id.ratingbar_small);
            this.img = (ImageView)  v.findViewById(R.id.item_beer_img);
        }

        @Override
        public void onClick(View v) {

        }

        // Source: http://stackoverflow.com/a/24933117/2637528
        public static interface ViewHolderBeerClick {

        }
    }
}

