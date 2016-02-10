package be.dieterholvoet.beerguide.model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.dieterholvoet.beerguide.helper.ImageStore;

/**
 * Created by Dieter on 26/12/2015.
 */

@Table(name = "Beers")
public class Beer extends Model implements Serializable {
    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "favorite")
    private boolean favorite;
    
    @Column(name = "rating")
    private BeerRating rating;

    @Column(name = "bdb")
    private BreweryDBBeer bdb;

    @Column(name = "notes")
    private String notes;

    private List<ImageStore> pictures;

    public Beer() {
        super();
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
        this.pictures = new ArrayList<>();
    }

    public Beer(BreweryDBBeer bdb) {
        super();
        this.rating = new BeerRating();
        this.bdb = bdb;
        this.pictures = new ArrayList<>();
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

    public List<ImageStore> getPicturesFromDB() {
        return new Select()
                .from(ImageStore.class)
                .where("beer = ?", this.getId())
                .execute();
        // return getMany(ImageStore.class, "beerFK");
    }

    public void setPicturesFromDB() {
        pictures = getPicturesFromDB();
    }

    public void addPicture(Uri uri, Activity context) {
        addPicture(new ImageStore(uri.toString(), this));
    }

    public void addPicture(ImageStore image) {
        image.setBeer(this);
        pictures.add(image);
    }

    public List<ImageStore> getPictures() {
        return pictures;
    }

    public void log() {
        Log.e("BEER", "Timestamp is " + this.timestamp == null ? "null" : String.valueOf(timestamp));
        Log.e("BEER", "Favorite " + this.favorite == null ? "null" : "not null");
        Log.e("BEER", "Rating is " + this.rating == null ? "null" : "not null");
        Log.e("BEER", "BDB beer is " + this.bdb == null ? "null" : "not null");

        if(this.bdb != null) {
            Log.e("BEER", "Description is " + this.bdb.getDescription() == null ? "null" : this.bdb.getDescription());
        }
    }

    public boolean exists() {
        return this.getId() != null;
    }
}
