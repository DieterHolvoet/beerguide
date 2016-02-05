package be.dieterholvoet.beerguide.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import be.dieterholvoet.beerguide.bus.BeerListTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */

public class BeerListTask extends AsyncTask<Void, Void, List<Beer>> {
    Context context;

    public BeerListTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Beer> doInBackground(Void... params) {
        List<Beer> beers = BeerDAO.getAll();
        if(BreweryDB.isNetworkAvailable(context) && beers != null && beers.size() > 0) {
            beers = BreweryDB.getInstance().getBreweryDBData(beers);
        }
        return beers;
    }

    @Override
    protected void onPostExecute(List<Beer> beers) {
        EventBus.getInstance().post(new BeerListTaskEvent(beers));
    }
}
