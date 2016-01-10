package be.dieterholvoet.beerguide.bus;

import java.util.List;

import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BeerListTaskEvent {

    private List<Beer> beers;

    public BeerListTaskEvent(List<Beer> beers) {
        this.beers = beers;
    }

    public List<Beer> getBeers() {
        return beers;
    }
}
