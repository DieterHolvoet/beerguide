package be.dieterholvoet.beerguide.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.tasks.RecentBeerListTask;

public class BeersAllFragment extends Fragment {
    View view;
    RecyclerView recycler;
    ProgressDialog progress;
    SwipeRefreshLayout swipeRefreshLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_beers_all, null);
        this.recycler = (RecyclerView) view.findViewById(R.id.fragment_beers_all_recycler);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_beers_all_swipe);

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("LOG", "Refreshing...");
                new RecentBeerListTask(getActivity()).execute();
            }
        });

        progress = ProgressDialog.show(getActivity(), "", "Loading beers...", true);
        new RecentBeerListTask(getActivity()).execute();

        return view;
    }

    @Subscribe
    public void onRecentBeerListTaskResult(RecentBeerListTaskEvent event) {
        recycler.setAdapter(new BeerListAdapter(event.getBeers(), getActivity()));
        progress.dismiss();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BeerCardCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recycler));
        itemTouchHelper.attachToRecyclerView(recycler);

        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            Log.e("LOG", "Refreshing done.");
        }
    }
}
