package be.dieterholvoet.beerguide.model;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Dieter on 11/01/2016.
 */

public class BreweryDBAvailability extends RealmObject implements Serializable {
    @PrimaryKey
    private long primaryKey;

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String description;

    public BreweryDBAvailability() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public BreweryDBAvailability save(Realm realm) {
        BreweryDBAvailability newObj;
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();
            if(this.primaryKey == 0) throw new RealmPrimaryKeyConstraintException("");

        } catch(RealmPrimaryKeyConstraintException e) {
            this.primaryKey = PrimaryKeyFactory.getInstance().nextKey(this.getClass());

        } finally {
            newObj = realm.copyToRealm(this);
            if(!inTransactionBefore) realm.commitTransaction();
        }

        return newObj;
    }

    public void delete(Realm realm) {
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();
            if(this.primaryKey == 0) throw new Exception("Cannot delete: this is not a managed Realm object.");

        } catch(Exception e) {
            Log.d("BEER", e.getMessage());
            return;

        } finally {
            this.deleteFromRealm();
            if(!inTransactionBefore) realm.commitTransaction();
        }
    }
}
