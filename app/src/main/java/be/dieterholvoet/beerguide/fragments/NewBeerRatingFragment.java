package be.dieterholvoet.beerguide.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.Toast;

import be.dieterholvoet.beerguide.MainActivity;
import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.db.DB;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BeerRating;

/**
 * Created by Dieter on 26/12/2015.
 */

public class NewBeerRatingFragment extends Fragment {
    NewBeerActivity activity;
    CardView button;
    RatingBar overallRating;
    Beer beer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View v = inflater.inflate(R.layout.fragment_new_beer_rating, null);
        this.activity = (NewBeerActivity) getActivity();
        this.beer = activity.getBeer();
        this.overallRating = (RatingBar) v.findViewById(R.id.overall_rating_card_rating);
        this.button = (CardView) v.findViewById(R.id.overall_rating_card_submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beer.getRating().getRating() == 0) {
                    Toast.makeText(getActivity(), "Please give an overall rating before saving.", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("BEER", "Name: " + beer.getBdb().getName());
                    Log.e("BEER", "ID: " + beer.getBdb().getBreweryDBID());
                    beer.save();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });

        overallRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int rating_num = Math.round(ratingBar.getRating());
                BeerRating beerRating = beer.getRating();
                beerRating.setRating(rating_num);
                beer.setRating(beerRating);
            }
        });

        return v;
    }
}
