package be.dieterholvoet.beerguide.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import be.dieterholvoet.beerguide.bus.BeerListTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */

public class RecentBeerListTask extends AsyncTask<Void, Void, List<Beer>> {

    @Override
    protected List<Beer> doInBackground(Void... params) {
        List<Beer> beers = Beer.getRecent();

        for(Beer beer : beers) {
            BreweryDBBeer bdb = beer.getBdb();
            if(bdb.getYear() == null || bdb.getYear().isEmpty()) {
                if(bdb.getBreweryDBID() == null) {
                    Log.e("API", "No BreweryDB ID provided.");

                } else {
                    beer.setBdb(BreweryDB.getInstance().getBeerByID(bdb.getBreweryDBID()));
                    beer.save();
                }
            }
        }
        return beers;
    }

    @Override
    protected void onPostExecute(List<Beer> beers) {
        EventBus.getInstance().post(new RecentBeerListTaskEvent(beers));
    }
}
