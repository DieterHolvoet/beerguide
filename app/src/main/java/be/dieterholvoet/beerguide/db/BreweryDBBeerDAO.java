package be.dieterholvoet.beerguide.db;

import android.util.Log;

import com.activeandroid.query.Select;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;

/**
 * Created by Dieter on 23/01/2016.
 */
public class BreweryDBBeerDAO {

    public static Beer getBeer(String id) {
        Beer beer = null;
        BreweryDBBeer bdb = new Select()
                .from(BreweryDBBeer.class)
                .where("BreweryDBID = ?", id)
                .executeSingle();

        if(bdb != null) {
            beer = new Select()
                    .from(Beer.class)
                    .where("bdb = ?", bdb)
                    .executeSingle();
        }

        if(beer != null) {
            Log.e("BEER", "Found beer: " + beer.getBdb().getName());
            return beer;

        } else {
            return null;
        }
    }
}
