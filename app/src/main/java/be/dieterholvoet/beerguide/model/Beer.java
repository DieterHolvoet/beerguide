package be.dieterholvoet.beerguide.model;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import be.dieterholvoet.beerguide.helper.ImageStore;
import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
import be.dieterholvoet.beerguide.helper.RealmCascadedActions;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Dieter on 26/12/2015.
 */

public class Beer extends RealmObject implements Serializable, RealmCascadedActions {
    @PrimaryKey
    private long primaryKey;
    private long timestamp;
    private boolean favorite;
    private BeerRating rating;
    private BreweryDBBeer bdb;
    private String notes;

    private RealmList<ImageStore> pictures;

    public Beer() {
        super();
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
        this.pictures = new RealmList<>();
    }

    public Beer(BreweryDBBeer bdb) {
        super();
        this.rating = new BeerRating();
        this.bdb = bdb;
        this.pictures = new RealmList<>();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public BreweryDBBeer getBdb() {
        return bdb;
    }

    public void setBdb(BreweryDBBeer bdb) {
        this.bdb = bdb;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public BeerRating getRating() {
        return rating;
    }

    public void setRating(BeerRating rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addPicture(Uri uri) {
        addPicture(new ImageStore(uri.toString()));
    }

    public void addPicture(ImageStore image) {
        pictures.add(image);
    }

    public List<ImageStore> getPictures() {
        return pictures;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean exists() {
        return this.primaryKey != 0;
    }

    public void log() {
        Log.e("BEER", "Timestamp is " + (this.timestamp == 0 ? "0" : String.valueOf(timestamp)));
        Log.e("BEER", "Favorite " + (this.favorite ? "true" : "false"));
        Log.e("BEER", "Rating is " + (this.rating == null ? "null" : this.rating.toString()));
        Log.e("BEER", "BreweryDBBeer is " + (this.bdb == null ? "null" : this.bdb.toString()));
        Log.e("BEER", "Style is " + (this.bdb.getStyle() == null ? "null" : this.bdb.getStyle()));

        if(this.bdb != null) {
            Log.e("BEER", "Description is " + (this.bdb.getDescription() == null ? "null" : this.bdb.getDescription()));

            if(this.bdb.getStyle() != null) {
                Log.e("BEER", "Category is " + (this.bdb.getStyle().getCategory() == null ? "null" : this.bdb.getStyle().getCategory()));
            }
        }
    }

    public Beer save(Realm realm) {
        Beer newObj;
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();
            if(this.bdb != null) this.bdb = this.bdb.save(realm);
            if(this.rating != null) this.rating = this.rating.save(realm);

            if(this.primaryKey == 0) {
                this.timestamp = Calendar.getInstance().getTimeInMillis();
                throw new RealmPrimaryKeyConstraintException("");
            }

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
            if(this.bdb != null) this.bdb.delete(realm);
            if(this.rating != null) this.rating.delete(realm);

        } catch(Exception e) {
            Log.d("BEER", e.getMessage());
            return;

        } finally {
            this.deleteFromRealm();
            if(!inTransactionBefore) realm.commitTransaction();
        }
    }

    public void savePictures(Realm realm) {
        realm.beginTransaction();

        if(this.pictures != null && !this.pictures.isEmpty()) {
            Log.e("BEER", "Saving " + this.pictures.size() + " pictures!");

            for(ImageStore picture : this.pictures) {
                realm.copyToRealm(picture);
            }

        } else {
            Log.e("BEER", "No pictures to save!");
        }

        realm.commitTransaction();
    }
}