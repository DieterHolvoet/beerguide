package be.dieterholvoet.beerguide.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import be.dieterholvoet.beerguide.bus.EndPointAvailableEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */
public class EndpointAvailabilityCheckTask extends AsyncTask<Void, Void, Void> {
    Context context;

    public EndpointAvailabilityCheckTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        BreweryDB.getInstance().getBeerByID("ZuNwhx");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        boolean available = BreweryDB.isNetworkAvailable(context) && !BreweryDB.isDailyLimitReached();
        Log.e("LOG", "Endpoint is " + (available ? "available" : "not available") + ". Posting event.");
        EventBus.getInstance().post(new EndPointAvailableEvent(available));
    }
}