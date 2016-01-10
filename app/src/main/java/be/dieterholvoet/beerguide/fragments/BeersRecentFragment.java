package be.dieterholvoet.beerguide.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import be.dieterholvoet.beerguide.BeerCardCallback;
import be.dieterholvoet.beerguide.ItemOffsetDecoration;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.SpacesItemDecoration;
import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.tasks.RecentBeerListTask;

public class BeersRecentFragment extends Fragment {
    RecyclerView recycler;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getInstance().register(this);
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_beers_recent, null);

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

        recycler.setHasFixedSize(true);
        recycler.setAdapter(new BeerListAdapter(new ArrayList(), getActivity()));

        progress = ProgressDialog.show(getActivity(), "", "Loading beers...", true);

        new RecentBeerListTask().execute();

        return recycler;
    }

    @Subscribe
    public void onRecentBeerListTaskResult(RecentBeerListTaskEvent event) {
        recycler.setAdapter(new BeerListAdapter(event.getBeers(), getActivity()));
        progress.dismiss();

        if(event.getBeers().size() > 0) {
            Log.e("BEER", "Name: " + event.getBeers().get(0).getBdb().getName());
            Log.e("BEER", "Rating: " + event.getBeers().get(0).getRating().getRating());
            Log.e("BEER", "Category: " + event.getBeers().get(0).getBdb().getStyle().getCategory().getName());
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BeerCardCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recycler));
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void onDestroyView() {
        EventBus.getInstance().unregister(this);
        super.onDestroyView();
    }
}
