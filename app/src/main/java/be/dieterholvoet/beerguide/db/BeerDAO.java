package be.dieterholvoet.beerguide.db;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Condition;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 23/01/2016.
 */

public class BeerDAO {

    public static Beer getByBreweryDBID(String id) {
        Beer beer = null;

        BreweryDBBeer bdb = new Select()
                .from(BreweryDBBeer.class)
                .where("BreweryDBID = ?", id)
                .executeSingle();

        if(bdb != null) {
            beer = new Select()
                    .from(Beer.class)
                    .where("bdb = ?", bdb.getId())
                    .executeSingle();
        }

        return beer;
    }

    public static List<Beer> getAll() {
        return getAll(10);
    }

    public static List<Beer> getAll(int limit) {
        return new Select()
                .from(Beer.class)
                .limit(limit)
                .execute();
    }

    public static List<Beer> getRecent() {
        return getRecent(10, 10);
    }

    public static List<Beer> getRecent(int daysCount, int limit) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysCount);

        return new Select()
                .from(Beer.class)
                .where("timestamp > ?", cal.getTimeInMillis())
                .limit(limit)
                .execute();
    }

    public static void delete(Beer beer) {
        if (beer.getBdb() != null) {
            if(beer.getRating() != null) {
                beer.getRating().delete();
            }

            if(beer.getBdb().getLabels() != null) {
                beer.getBdb().getLabels().delete();
            }

            if(beer.getBdb().getAvailability() != null) {
                beer.getBdb().getAvailability().delete();
            }

            if(beer.getBdb().getSrm() != null) {
                beer.getBdb().getSrm().delete();
            }

            beer.getBdb().delete();
        }

        beer.delete();
        ActiveAndroid.clearCache();
        Log.e("BEER", "Beer deleted");
    }

    public static void save(Beer beer) {
        if(beer.getTimestamp() == 0) {
            beer.setTimestamp(Calendar.getInstance().getTimeInMillis());
            Log.e("BEER", "Saving new beer!");

        } else {
            //beer.delete();
            Log.e("BEER", "Updating beer: " + beer.getBdb().getName());

            // Dirty fix for duplicates
            // Beer.executeQuery("DELETE FROM BEER WHERE ADDED = ?", new String[]{String.valueOf(this.getAdded())});

        }

        if(beer.getBdb() == null) {
            Log.e("BEER", "BreweryDBBeer of beer is null.");

        } else {
            if(beer.getRating() == null) {
                Log.e("BEER", "Rating of beer is null.");

            } else {
                beer.getRating().save();
            }

            if(beer.getBdb().getStyle() == null) {
                Log.e("BEER", "Style of beer is null.");

            } else {
                if(beer.getBdb().getStyle().getCategory() == null) {
                    Log.e("BEER", "Category of beer is null.");

                } else {
                    beer.getBdb().getStyle().getCategory().save();
                }

                beer.getBdb().getStyle().save();
            }

            if(beer.getBdb().getLabels() == null) {
                Log.e("BEER", "Label collection of beer is null.");

            } else {
                beer.getBdb().getLabels().save();
            }

            if(beer.getBdb().getAvailability() == null) {
                Log.e("BEER", "Availability of beer is null.");

            } else {
                beer.getBdb().getAvailability().save();
            }

            if(beer.getBdb().getSrm() == null) {
                Log.e("BEER", "SRM of beer is null.");

            } else {
                beer.getBdb().getSrm().save();
            }

            if(beer.getBdb().getBrewery() == null) {
                Log.e("BEER", "Brewery of beer is null.");

            } else {
                beer.getBdb().getBrewery().save();
            }

            beer.getBdb().save();
        }

        beer.save();
    }
}
