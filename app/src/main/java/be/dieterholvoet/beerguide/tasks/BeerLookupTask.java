package be.dieterholvoet.beerguide.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BeerLookupTask extends AsyncTask<Void, Void, Void> {
    Context context;
    private ArrayList<Beer> beers;

    public BeerLookupTask(Context context) {
        this.context = context;
    }

    public BeerLookupTask(Context context, Beer beer) {
        this.context = context;
        this.beers = new ArrayList<Beer>(Arrays.asList(beer));
    }

    public BeerLookupTask(Context context, ArrayList<Beer> beers) {
        this.context = context;
        this.beers = beers;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();

        if(BreweryDB.isNetworkAvailable(context)) {
            if(this.beers == null) {
                final RealmResults<Beer> realmResults = BeerDAO.getAll(realm);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for(Beer beer : realmResults) {
                            BreweryDB.getInstance().setBreweryDBData(beer);
                        }
                    }
                });

            } else {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for(int i = 0; i < beers.size(); i++) {
                            if(beers.get(i).exists()) {
                                Log.d("ERROR", "Object is managed, unable to update!");
                            } else {
                                beers.set(i, BreweryDB.getInstance().setBreweryDBData(beers.get(i)));
                            }
                        }
                    }
                });
            }

        } else {
            Log.e("LOOKUPTASK", "Network not available");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(this.beers == null) {
            EventBus.getInstance().post(new BeerLookupTaskEvent());

        } else {
            EventBus.getInstance().post(new BeerLookupTaskEvent(beers));
        }
    }
}