package be.dieterholvoet.beerapp.model;

import android.provider.BaseColumns;

import java.sql.Date;

/**
 * Created by Dieter on 26/12/2015.
 */
public class Beer {
    private int _id;
    private BreweryDBBeer bdb;
    private BeerRating rating;
    private Date dateAdded;
    private boolean favorite;

    public Beer(BreweryDBBeer bdb, BeerRating rating, Date dateAdded, boolean favorite) {
        this.bdb = bdb;
        this.dateAdded = dateAdded;
        this.favorite = favorite;
        this.rating = rating;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public BreweryDBBeer getBdb() {
        return bdb;
    }

    public void setBdb(BreweryDBBeer bdb) {
        this.bdb = bdb;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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

    public static abstract class Contract implements BaseColumns {
        public static final String TABLE_NAME = "beers";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_BDB_ID = "bdb_id";
        public static final String COLUMN_NAME_RATING_ID = "rating_id";
        public static final String COLUMN_NAME_DATE_ADDED = "date_added";
        public static final String COLUMN_NAME_FAVORITE = "favorite";

        public static final String[] TABLE_COLUMNS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_BDB_ID,
                COLUMN_NAME_RATING_ID,
                COLUMN_NAME_DATE_ADDED,
                COLUMN_NAME_FAVORITE
        };
    }
}
