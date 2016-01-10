package be.dieterholvoet.beerguide.bus;

import android.util.Log;

import java.util.List;

import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 9/01/2016.
 */
public class RecentBeerListTaskEvent {

    private List<Beer> beers;

    public RecentBeerListTaskEvent(List<Beer> beers) {
        this.beers = beers;
    }

    public List<Beer> getBeers() {
        return beers;
    }
}
