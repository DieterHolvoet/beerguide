package be.dieterholvoet.beerguide.tasks;

import android.os.AsyncTask;

import java.util.ArrayList;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BeerLookupTask extends AsyncTask<Void, Void, Void> {

    private BreweryDB API;
    private Beer beer;

    public BeerLookupTask(Beer beer) {
        this.beer = beer;
        this.API = BreweryDB.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params) {
        beer.setBdb(API.getBeerByID(beer.getBdb().getBreweryDBID()));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }
}