package be.dieterholvoet.beerguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBCategory;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by Dieter on 7/09/2016.
 */

public class BeerListAdapter
        extends RealmBasedRecyclerViewAdapter<Beer, BeerListAdapter.ViewHolder> {

    private View rootView;

    public class ViewHolder extends RealmViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView category;
        protected ImageView img;
        protected RatingBar rating;
        protected Activity activity;

        public ViewHolder(View v, Activity a) {
            super(v);
            v.setOnClickListener(this);
            this.activity = a;
            this.name =  (TextView) v.findViewById(R.id.item_beer_name);
            this.category = (TextView)  v.findViewById(R.id.item_beer_category_text);
            this.rating = (RatingBar)  v.findViewById(R.id.ratingbar_small);
            this.img = (ImageView)  v.findViewById(R.id.item_beer_img);
        }

        @Override
        public void onClick(View v) {
            Log.e("LOG", "Click!");
            Beer beer = realmResults.get(getAdapterPosition());
            Intent intent = new Intent(activity, NewBeerActivity.class);
            Bundle b = new Bundle();

            b.putString("bdbID", beer.getBdb().getBreweryDBID());
            b.putString("beerName", beer.getBdb().getName());
            intent.putExtras(b);

            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    public BeerListAdapter(
            Context context,
            View rootView,
            RealmResults<Beer> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.rootView = rootView;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_beer, viewGroup, false);
        return new ViewHolder(v, (Activity) getContext());
    }

    @Override
    public void onBindRealmViewHolder(final ViewHolder viewHolder, int position) {

        Beer beer = realmResults.get(position);
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

            if(category == null) {
                category = new BreweryDBCategory();
            }
        }

        String name = bdb.getName();
        String cat = (category.getName() == null || "".equals(category.getName())) ? "unknown" : category.getName();
        float rating = beer.getRating().getRating();

        viewHolder.name.setText(name);
        viewHolder.category.setText(cat);
        viewHolder.rating.setRating(rating);

        if(bdb.getLabels()!= null) {
            Picasso.with(getContext())
                    .load(bdb.getLabels().getMedium())
                    .placeholder(R.drawable.beer_placeholder)
                    .resize(200, 267)
                    .centerCrop()
                    .into(viewHolder.img);
        } else {

            Picasso.with(getContext())
                    .load(R.drawable.beer_placeholder)
                    .resize(200, 267)
                    .centerCrop()
                    .into(viewHolder.img);
        }

        Drawable starDrawable = ContextCompat.getDrawable(getContext(), R.drawable.star_small_full);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.rating.getLayoutParams();
        params.height = starDrawable.getMinimumHeight();
        viewHolder.rating.setLayoutParams(params);

        viewHolder.name.post(new Runnable() {
            @Override
            public void run() {
                if (viewHolder.name.getLineCount() == 2 && viewHolder.category.getLineCount() == 2) {
                    viewHolder.category.setMaxLines(1);
                }
            }
        });
    }

    @Override
    public void onItemSwipedDismiss(final int position) {
        final Beer beer = getAt(position);
        final Snackbar snackbar = Snackbar.make(this.rootView, "Beer removed.", Snackbar.LENGTH_LONG);

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Realm realm = Realm.getDefaultInstance();
                beer.delete(realm);
                realm.close();
            }
        });

        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                snackbar.setCallback(null);
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    // Source: http://stackoverflow.com/a/26310638/2637528
    public void removeAt(int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realmResults.get(position).deleteFromRealm();
        realm.commitTransaction();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, realmResults.size());

        realm.close();
    }

    public Beer getAt(int position) {
        if(realmResults.size() > 0) {
            return realmResults.get(position);

        } else {
            Log.e("LOG", "Can't get beer from empty list");
            return null;
        }
    }
}
