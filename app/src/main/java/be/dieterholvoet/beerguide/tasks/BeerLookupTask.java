package be.dieterholvoet.beerguide.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BeerLookupTask extends AsyncTask<Void, Void, Void> {
    Context context;
    private BreweryDB API;
    private Beer beer;

    public BeerLookupTask(Context context, Beer beer) {
        this.context = context;
        this.beer = beer;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(BreweryDB.isNetworkAvailable(context)) {
            beer = BreweryDB.getInstance().getBreweryDBData(beer);

        } else {
            Log.e("LOOKUPTASK", "Network not available");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getInstance().post(new BeerLookupTaskEvent(beer));
    }
}