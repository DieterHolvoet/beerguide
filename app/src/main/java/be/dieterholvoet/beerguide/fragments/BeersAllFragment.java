package be.dieterholvoet.beerguide.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import be.dieterholvoet.beerguide.SimpleItemTouchHelperCallback;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.bus.BeerListTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.tasks.BeerListTask;

public class BeersAllFragment extends Fragment {
    View view;
    RecyclerView recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    Snackbar snackbar;
    
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
                new BeerListTask(getActivity()).execute();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        snackbar = Snackbar.make(getView(), getResources().getString(R.string.dialog_loading_beers), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        new BeerListTask(getActivity()).execute();
    }

    @Subscribe
    public void onBeerListTaskResult(final BeerListTaskEvent event) {
        BeerListAdapter adapter = new BeerListAdapter(event.getBeers(), getActivity());
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recycler, adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        snackbar.dismiss();
        recycler.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(recycler);

        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            Log.e("LOG", "Refreshing done.");
        }
    }
}
