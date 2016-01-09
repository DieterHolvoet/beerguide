package be.dieterholvoet.beerapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.model.Beer;
import be.dieterholvoet.beerapp.model.BreweryDBBeer;
import be.dieterholvoet.beerapp.model.BreweryDBCategory;

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
        BreweryDBBeer bdb = beer.getBdb();
        BreweryDBCategory category = bdb.getStyle().getCategory();

        ratingViewHolder.name.setText(bdb.getName());
        ratingViewHolder.category.setText(category.getName());
        ratingViewHolder.year.setText(bdb.getYear());
        // ratingViewHolder.img.setImageResource();
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
        protected TextView year;
        protected ImageView img;

        public BeerListViewHolder(View v, ViewHolderBeerClick listener) {
            super(v);
            this.listener = listener;
            this.name =  (TextView) v.findViewById(R.id.item_beer_name);
            this.category = (TextView)  v.findViewById(R.id.item_beer_category_text);
            this.year = (TextView)  v.findViewById(R.id.item_beer_year_text);
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

