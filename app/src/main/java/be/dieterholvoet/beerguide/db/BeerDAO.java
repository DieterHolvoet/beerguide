package be.dieterholvoet.beerguide.db;

import android.util.Log;

import be.dieterholvoet.beerguide.helper.BeerSort;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.ImageStore;
import io.realm.Realm;
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
}
