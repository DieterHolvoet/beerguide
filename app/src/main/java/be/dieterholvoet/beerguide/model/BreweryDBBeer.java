package be.dieterholvoet.beerguide.model;

import android.util.Log;

import java.io.Serializable;

import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResultBeer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBBeer extends RealmObject implements Serializable {
    @PrimaryKey
    private long primaryKey;

    private String BreweryDBID;
    private String name;
    private String year;
    private BreweryDBLabel labels;
    private BreweryDBStyle style;
    private BreweryDBAvailability availability;
    private BreweryDBSRM srm;
    private BreweryDBBrewery brewery;
    private String abv;
    private String ibu;
    private String og;
    private String description;

    public BreweryDBBeer() {
        super();
    }

    public BreweryDBBeer(String BreweryDBID) {
        super();
        this.BreweryDBID = BreweryDBID;
    }

    public BreweryDBBeer(BreweryDBResultBeer result) {
        super();
        this.BreweryDBID = result.getId();
        this.name = result.getName();
        this.year = result.getYear();
        this.labels = result.getLabels();
        this.style = result.getStyle();
        this.availability = result.getAvailability();
        this.srm = result.getSrm();
        this.brewery = result.getBrewery();
        this.abv = result.getAbv();
        this.ibu = result.getIbu();
        this.og = result.getOg();
        this.description = result.getDescription();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getBreweryDBID() {
        return BreweryDBID;
    }

    public void setBreweryDBID(String breweryDBID) {
        BreweryDBID = breweryDBID;
    }

    public BreweryDBLabel getLabels() {
        return labels;
    }

    public void setLabels(BreweryDBLabel labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BreweryDBStyle getStyle() {
        return style;
    }

    public void setStyle(BreweryDBStyle style) {
        this.style = style;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public BreweryDBAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(BreweryDBAvailability availability) {
        this.availability = availability;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getOg() {
        return og;
    }

    public void setOg(String og) {
        this.og = og;
    }

    public BreweryDBSRM getSrm() {
        return srm;
    }

    public void setSrm(BreweryDBSRM srm) {
        this.srm = srm;
    }

    public BreweryDBBrewery getBrewery() {
        return brewery;
    }

    public void setBrewery(BreweryDBBrewery brewery) {
        this.brewery = brewery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public BreweryDBBeer save(Realm realm) {
        BreweryDBBeer newObj;
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();

            if(this.labels != null) this.labels = this.labels.save(realm);
            if(this.style != null) this.style = this.style.save(realm);
            if(this.availability != null) this.availability = this.availability.save(realm);
            if(this.srm != null) this.srm = this.srm.save(realm);
            if(this.brewery != null) this.brewery = this.brewery.save(realm);

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

            if(this.labels != null) this.labels.delete(realm);
            if(this.style != null) this.style.delete(realm);
            if(this.availability != null) this.availability.delete(realm);
            if(this.srm != null) this.srm.delete(realm);
            if(this.brewery != null) this.brewery.delete(realm);

        } catch(Exception e) {
            Log.d("BEER", e.getMessage());
            return;

        } finally {
            this.deleteFromRealm();
            if(!inTransactionBefore) realm.commitTransaction();
        }
    }
}