package be.dieterholvoet.beerguide.helper;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Dieter on 7/09/2016.
 * Source: https://gist.github.com/RaghavThakkar/87027cb272804d2dc1fdde253c77ac90
 */

public class PrimaryKeyFactory {


    /**
     * primary key field name
     */
    public static final String PRIMARY_KEY_FIELD = "primaryKey";

    /**
     * Singleton instance.
     */
    private final static PrimaryKeyFactory instance = new PrimaryKeyFactory();

    /**
     * Maximum primary key values.
     */
    private Map<Class<? extends RealmModel>, AtomicLong> keys;

    /**
     * get the singleton instance
     *
     * @return singleton instance
     */
    public static PrimaryKeyFactory getInstance() {
        return instance;
    }

    /**
     * Initialize the factory. Must be called before any primary key is generated
     * - preferably from application class.
     */
    public synchronized void initialize(final Realm realm) {
        if (keys != null) {
            throw new IllegalStateException("already initialized");
        }
        keys = new HashMap<>();

        final RealmConfiguration configuration = realm.getConfiguration();
        final RealmSchema realmSchema = realm.getSchema();

        for (final Class<? extends RealmModel> c : configuration.getRealmObjectClasses()) {

            final RealmObjectSchema objectSchema = realmSchema.get(c.getSimpleName());
            if (objectSchema != null && objectSchema.hasPrimaryKey()) {
                Number keyValue = null;
                try {
                    keyValue = realm.where(c).max(PRIMARY_KEY_FIELD);
                } catch (ArrayIndexOutOfBoundsException ex) { }

                if (keyValue == null) {
                    keys.put(c, new AtomicLong(0));
                } else {
                    keys.put(c, new AtomicLong(keyValue.longValue()));
                }
            }
        }
    }

    /**
     * Automatically create next key for a given class.
     */
    public synchronized long nextKey(final Class<? extends RealmObject> clazz) {

        if (keys == null) {
            throw new IllegalStateException("not initialized yet");
        }
        AtomicLong l = keys.get(clazz);
        if (l == null) {
            Log.i(getClass().getSimpleName(), "There was no primary keys for " + clazz.getName());
            //RealmConfiguration#getRealmObjectClasses() returns only classes with existing instances
            //so we need to store value for the first instance created
            l = new AtomicLong(0);
            keys.put(clazz, l);
        }
        return l.incrementAndGet();
    }
}