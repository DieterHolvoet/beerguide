package be.dieterholvoet.beerguide.helper;

import io.realm.Realm;

/**
 * Created by Dieter on 8/09/2016.
 */

public interface RealmCascadedActions<E> {
    public E save(Realm realm);
    public void delete(Realm realm);
}