package be.dieterholvoet.beerguide.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Calendar;

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

    public Beer() {
        super();
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
    }

    public Beer(BreweryDBBeer bdb) {
        super();
        this.rating = new BeerRating();
        this.bdb = bdb;
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

    public void log() {
        Log.e("BEER", "Timestamp is " + this.timestamp == null ? "null" : String.valueOf(timestamp));
        Log.e("BEER", "Favorite " + this.favorite == null ? "null" : "not null");
        Log.e("BEER", "Rating is " + this.rating == null ? "null" : "not null");
        Log.e("BEER", "BDB beer is " + this.bdb == null ? "null" : "not null");

        if(this.bdb != null) {
            Log.e("BEER", "Description is " + this.bdb.getDescription() == null ? "null" : this.bdb.getDescription());
        }
    }
}
