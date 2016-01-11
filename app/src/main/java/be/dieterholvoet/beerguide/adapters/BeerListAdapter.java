package be.dieterholvoet.beerguide.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.squareup.picasso.Picasso;

import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBCategory;

/**
 * Created by Dieter on 8/01/2016.
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerListViewHolder> {

    private static List<Beer> beers;
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
    public void onBindViewHolder(final BeerListViewHolder ratingViewHolder, int i) {
        Beer beer = beers.get(i);
        final BreweryDBBeer bdb;
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

        if(bdb.getLabels()!= null) {
            Picasso.with(activity)
                    .load(bdb.getLabels().getMedium())
                    .placeholder(R.drawable.beer_placeholder)
                    .resize(200, 267)
                    .centerCrop()
                    .into(ratingViewHolder.img);
        } else {

            Picasso.with(activity)
                    .load(R.drawable.beer_placeholder)
                    .resize(200, 267)
                    .centerCrop()
                    .into(ratingViewHolder.img);
        }


        Drawable starDrawable = ContextCompat.getDrawable(activity, R.drawable.star_small_full);
        LayoutParams params = (LayoutParams) ratingViewHolder.rating.getLayoutParams();
        params.height = starDrawable.getMinimumHeight();
        ratingViewHolder.rating.setLayoutParams(params);

        ratingViewHolder.name.post(new Runnable() {
            @Override
            public void run() {
                if (ratingViewHolder.name.getLineCount() == 2 && ratingViewHolder.category.getLineCount() == 2) {
                    ratingViewHolder.category.setMaxLines(1);
                }
            }
        });
    }

    @Override
    public BeerListAdapter.BeerListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_beer, viewGroup, false);

        return new BeerListViewHolder(v);
    }

    public static class BeerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView category;
        protected ImageView img;
        protected RatingBar rating;

        public BeerListViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.name =  (TextView) v.findViewById(R.id.item_beer_name);
            this.category = (TextView)  v.findViewById(R.id.item_beer_category_text);
            this.rating = (RatingBar)  v.findViewById(R.id.ratingbar_small);
            this.img = (ImageView)  v.findViewById(R.id.item_beer_img);
        }

        @Override
        public void onClick(View v) {
            Log.e("LOG", "Click!");
            Beer beer = beers.get(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), NewBeerActivity.class);
            Bundle b = new Bundle();

            b.putSerializable("currentBeer", beer);
            intent.putExtras(b);

            v.getContext().startActivity(intent);
        }
    }

    // Source: http://stackoverflow.com/a/26310638/2637528
    public void removeAt(int position) {
        beers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, beers.size());
    }

    public Beer getAt(int position) {
        if(beers.size() > 0) {
            return beers.get(position);

        } else {
            Log.e("LOG", "Can't get beer from empty list");
            return null;
        }
    }

    public void setAt(int position, Beer beer) {
        beers.add(position, beer);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, beers.size());
    }
}

