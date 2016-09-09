package be.dieterholvoet.beerguide.model;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResultBrewery;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Dieter on 11/01/2016.
 */

public class BreweryDBBrewery extends RealmObject implements Serializable {
    @PrimaryKey
    private long primaryKey;

    @Expose
    private String BreweryDBBreweryID;
    @Expose
    private String descriptions;
    @Expose
    private String name;
    @Expose
    private int established;
    @Expose
    private String website;

    public BreweryDBBrewery() {
        super();
    }

    public BreweryDBBrewery(BreweryDBResultBrewery brewery) {
        super();
        this.BreweryDBBreweryID = brewery.getId();
        this.descriptions = brewery.getDescriptions();
        this.name = brewery.getName();
        this.established = brewery.getEstablished();
        this.website = brewery.getWebsite();
    }

    public String getBreweryDBBreweryID() {
        return BreweryDBBreweryID;
    }

    public void setBreweryDBBreweryID(String breweryDBBreweryID) {
        BreweryDBBreweryID = breweryDBBreweryID;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getEstablished() {
        return established;
    }

    public void setEstablished(int established) {
        this.established = established;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public BreweryDBBrewery save(Realm realm) {
        BreweryDBBrewery newObj;
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
