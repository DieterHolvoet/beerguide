package be.dieterholvoet.beerguide.db;

import android.util.Log;

import java.util.Calendar;
import java.util.List;

import be.dieterholvoet.beerguide.helper.BeerSort;
import be.dieterholvoet.beerguide.helper.ImageStore;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBCategory;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Dieter on 23/01/2016.
 */

public class BeerDAO {

    public static Beer getByBreweryDBID(Realm realm, String id) {
        return realm
                .where(Beer.class)
                .equalTo("bdb.BreweryDBID", id)
                .findFirst();
    }

    public static Beer getByPrimaryKey(Realm realm, long primaryKey) {
        return realm
                .where(Beer.class)
                .equalTo("primaryKey", primaryKey)
                .findFirst();
    }

    public static RealmResults<Beer> getAll(Realm realm) {
        return getAll(realm, BeerSort.PRIMARY_KEY, Sort.DESCENDING);
    }

    public static RealmResults<Beer> getRecent(Realm realm) {
        return getAll(realm, BeerSort.DATE_ADDED, Sort.DESCENDING);
    }

    public static RealmResults<Beer> getAll(Realm realm, String sorting, Sort direction) {
        return realm
                .where(Beer.class)
                .findAllSorted(sorting, direction);
    }

    public static void delete(Realm realm, Beer beer) {
        realm.beginTransaction();

        if (beer.getBdb() != null) {
            if(beer.getRating() != null) {
                beer.getRating().deleteFromRealm();
            }

            if(beer.getBdb().getLabels() != null) {
                beer.getBdb().getLabels().deleteFromRealm();
            }

            if(beer.getBdb().getAvailability() != null) {
                beer.getBdb().getAvailability().deleteFromRealm();
            }

            if(beer.getBdb().getSrm() != null) {
                beer.getBdb().getSrm().deleteFromRealm();
            }

            if(beer.getPictures() != null) {
                // beer.getPictures().delete();
                // new Delete().from(ImageStore.class).where("Id = ?", beer.)
            }

            beer.getBdb().deleteFromRealm();
        }

        beer.deleteFromRealm();
        Log.e("BEER", "Beer deleted");

        realm.commitTransaction();
    }

    public static void save(Realm realm, Beer beer) {
        beer.log();
        beer.save(realm);
        savePictures(realm, beer);
    }

    public static void savePictures(Realm realm, Beer beer) {

    }
}
