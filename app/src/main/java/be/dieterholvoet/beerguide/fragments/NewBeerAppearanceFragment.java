package be.dieterholvoet.beerguide.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.RatingAdapter;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BeerRating;
import be.dieterholvoet.beerguide.model.RatingElement;

/**
 * Created by Dieter on 26/12/2015.
 */
public class NewBeerAppearanceFragment extends Fragment {
    NewBeerActivity activity;
    Beer beer;
    BeerRating rating;
    RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = (NewBeerActivity) getActivity();
        this.beer = activity.getBeer();
        this.rating = beer.getRating();
        this.recycler = (RecyclerView) inflater.inflate(R.layout.fragment_new_beer_appearance, null);

        switch(getResources().getConfiguration().orientation) {
            // Portrait
            case 1:
                recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                break;

            // Landscape
            case 2:
                recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                break;
        }

        List<RatingElement> elements = new ArrayList<>();
        elements.add(new RatingElement("Foam", "How much would you rate the quality of the beer foam?", R.drawable.beer_foam_m, rating.getFoam()));
        elements.add(new RatingElement("Color", "Describe the color of the beer, ranging from very light to very dark.", R.drawable.beer_clearness_m, rating.getColor()));
        elements.add(new RatingElement("Clearness", "Describe the clearness of the beer, ranging from very clear to cloudy.", R.drawable.beer_clearness_2_m, rating.getClarity()));
        RatingAdapter ra = new RatingAdapter(elements, getActivity());

        recycler.setHasFixedSize(true);
        recycler.setAdapter(ra);

        return recycler;
    }
}
