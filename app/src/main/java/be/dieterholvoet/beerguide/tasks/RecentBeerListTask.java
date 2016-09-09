package be.dieterholvoet.beerguide.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import io.realm.Realm;

/**
 * Created by Dieter on 9/01/2016.
 */

public class RecentBeerListTask extends AsyncTask<Void, Void, List<Beer>> {
    Context context;
    List<Beer> beers;

    public RecentBeerListTask(Context context, List<Beer> beers) {
        this.context = context;
        this.beers = beers;
    }

    @Override
    protected List<Beer> doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();
        if(BreweryDB.isNetworkAvailable(context) && beers != null && beers.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    beers = BreweryDB.getInstance().setBreweryDBData(beers);
                }
            });

        }
        return beers;
    }

    @Override
    protected void onPostExecute(List<Beer> beers) {
        EventBus.getInstance().post(new RecentBeerListTaskEvent(beers));
    }
}
