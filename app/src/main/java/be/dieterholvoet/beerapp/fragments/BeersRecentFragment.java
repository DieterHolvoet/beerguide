package be.dieterholvoet.beerapp.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.dieterholvoet.beerapp.NewBeerActivity;
import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.adapters.BeerListAdapter;
import be.dieterholvoet.beerapp.adapters.RatingAdapter;
import be.dieterholvoet.beerapp.db.DB;
import be.dieterholvoet.beerapp.model.Beer;
import be.dieterholvoet.beerapp.model.RatingElement;

public class BeersRecentFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Beer> beers = DB.getInstance().getRecentBeers(10, 10);

        if(beers.isEmpty()) {
            return inflater.inflate(R.layout.fragment_beers_recent_placeholder, null);

        } else {
            RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_beers_recent, null);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            BeerListAdapter adapter = new BeerListAdapter(beers, getActivity());

            recycler.setHasFixedSize(true);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(llm);

            return recycler;
        }
    }
}
