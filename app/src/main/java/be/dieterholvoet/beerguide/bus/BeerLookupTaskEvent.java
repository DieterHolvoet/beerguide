package be.dieterholvoet.beerguide.bus;

import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 10/01/2016.
 */
public class BeerLookupTaskEvent {
    private Beer beer;

    public BeerLookupTaskEvent(Beer beer) {
        this.beer = beer;
    }

    public Beer getBeer() {
        return beer;
    }
}
