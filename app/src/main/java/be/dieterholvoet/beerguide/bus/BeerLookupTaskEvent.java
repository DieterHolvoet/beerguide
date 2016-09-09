package be.dieterholvoet.beerguide.bus;

import java.util.ArrayList;

import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 10/01/2016.
 */

public class BeerLookupTaskEvent {
    private ArrayList<Beer> beers;

    public BeerLookupTaskEvent() {}

    public BeerLookupTaskEvent(ArrayList<Beer> beers) {
        this.beers = beers;
    }

    public ArrayList<Beer> getBeers() {
        return beers;
    }
}
