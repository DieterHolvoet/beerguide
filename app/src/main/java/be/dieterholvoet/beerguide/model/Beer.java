package be.dieterholvoet.beerguide.model;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 26/12/2015.
 */

public class Beer extends SugarRecord {
    private long added;
    private boolean favorite;
    private BeerRating rating;
    private BreweryDBBeer bdb;

    public Beer() {
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
    }

    public Beer(long added, boolean favorite) {
        this.added = added;
        this.favorite = favorite;
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public BreweryDBBeer getBdb() {
        return bdb;
    }

    public void setBdb(BreweryDBBeer bdb) {
        this.bdb = bdb;
    }

    public BeerRating getRating() {
        return rating;
    }

    public void setRating(BeerRating rating) {
        this.rating = rating;
    }

    public static List<Beer> getAll() {
        return Beer.listAll(Beer.class);
    }

    public static List<Beer> getRecent() {
        return getRecent(10, 10);
    }

    public static List<Beer> getRecent(int daysCount, int resultCount) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyddMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysCount);

        long time = cal.getTimeInMillis() / 1000;
        return getAll();
        // return Select.from(Beer.class).where(Condition.prop("added").gt(time)).limit(String.valueOf(resultCount)).list();
    }

    @Override
    public long save() {
        if(this.getBdb() == null) {
            Log.e("BEER", "BreweryDBBeer of beer is null.");

        } else {
            if(this.getRating() == null) {
                Log.e("BEER", "Rating of beer is null.");

            } else {
                this.getRating().save();
            }

            if(this.getBdb().getStyle() == null) {
                Log.e("BEER", "Style of beer is null.");

            } else {
                if(this.getBdb().getStyle().getCategory() == null) {
                    Log.e("BEER", "Category of beer is null.");

                } else {
                    this.getBdb().getStyle().getCategory().save();
                }

                this.getBdb().getStyle().save();
            }

            if(this.getBdb().getLabels() == null) {
                Log.e("BEER", "Label collection of beer is null.");

            } else {
                this.getBdb().getLabels().save();
            }

            this.getBdb().save();
        }

        return super.save();
    }
}
