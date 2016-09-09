package be.dieterholvoet.beerguide.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.dieterholvoet.beerguide.MainActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.helper.BeersRecyclerFragmentType;
import be.dieterholvoet.beerguide.model.Beer;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class BeersRecyclerFragment extends Fragment {
    View view;
    RealmRecyclerView recycler;
    MainActivity activity;
    BeerListAdapter adapter;
    RealmResults<Beer> realmResults;
    Realm realm;
    BeersRecyclerFragmentType type;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if(getArguments().getSerializable("type") != null) {
            type = (BeersRecyclerFragmentType) getArguments().getSerializable("type");
        }
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
        this.activity = (MainActivity) getActivity();
        this.view = inflater.inflate(R.layout.fragment_beers_all, null);
        this.recycler = (RealmRecyclerView) view.findViewById(R.id.fragment_beers_all_recycler);
        this.realm = Realm.getDefaultInstance();

        switch(type) {
            case ALL:
                this.realmResults = BeerDAO.getAll(realm);
                break;

            case RECENT:
                this.realmResults = BeerDAO.getRecent(realm);
                break;
        }

        this.adapter = new BeerListAdapter(this.activity, activity.findViewById(android.R.id.content), realmResults, true, true);
        recycler.setAdapter(this.adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        realm = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
