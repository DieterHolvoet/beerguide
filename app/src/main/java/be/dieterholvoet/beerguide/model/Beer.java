package be.dieterholvoet.beerguide.model;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 26/12/2015.
 */

public class Beer extends SugarRecord implements Serializable {
    private long added;
    private boolean favorite;
    private BeerRating rating;
    private BreweryDBBeer bdb;

    public Beer() {
        this.rating = new BeerRating();
        this.bdb = new BreweryDBBeer();
    }

    public Beer(BreweryDBBeer bdb) {
        this.rating = new BeerRating();
        this.bdb = bdb;
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

        long time = cal.getTimeInMillis();
        return Select.from(Beer.class).where(Condition.prop("added").gt(time)).limit(String.valueOf(resultCount)).list();
    }

    public static List<Beer> getBreweryDBData(List<Beer> beers) {
        for(int i = 0; i < beers.size(); i++) {
            beers.set(i, getBreweryDBData(beers.get(i)));
        }
        return beers;
    }

    public static Beer getBreweryDBData(Beer beer) {
        BreweryDBBeer bdb = beer.getBdb();
        if(bdb == null) {
            Log.e("LOG", "Can't get bdb data, bdb is null!");
            return beer;

        } else {
            if(bdb.getDescription() == null) {
                if(bdb.getBreweryDBID() == null) {
                    Log.e("API", "No BreweryDB ID provided.");

                } else {
                    beer.setBdb(BreweryDB.getInstance().getBeerByID(bdb.getBreweryDBID()));
                    beer.getBdb().save();
                    Log.e("BEER", "ABV: " + beer.getBdb().getAbv());
                    beer.save();
                }
            }

            return beer;
        }
    }

    public static Beer getByBreweryDBID(String id) {
        BreweryDBBeer bdb = Select.from(BreweryDBBeer.class).where(Condition.prop("BREWERY_DBID").eq(id)).first();
        List<Beer> results = Beer.find(Beer.class, "BDB = ?", String.valueOf(bdb.getId()));
        if(results != null && results.size() != 0) {
            Log.e("BEER", "Found beer: " + results.get(0).getBdb().getName());
            return results.get(0);

        } else {
            return null;
        }

        /*Beer beer = Select.from(Beer.class).where(Condition.prop("BDB").eq(String.valueOf(bdb.getId()))).first();
        return beer;*/
    }

    @Override
    public long save() {
        if(this.getAdded() == 0) {
            this.setAdded(Calendar.getInstance().getTimeInMillis());
            Log.e("BEER", "Saving new beer!");

        } else {
            this.delete();
            Log.e("BEER", "Updating beer: " + this.getBdb().getName());

            // Dirty fix for duplicates
            Beer.executeQuery("DELETE FROM BEER WHERE ADDED = ?", new String[]{String.valueOf(this.getAdded())});

        }

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

            if(this.getBdb().getAvailability() == null) {
                Log.e("BEER", "Availability of beer is null.");

            } else {
                this.getBdb().getAvailability().save();
            }

            if(this.getBdb().getSrm() == null) {
                Log.e("BEER", "SRM of beer is null.");

            } else {
                this.getBdb().getSrm().save();
            }

            if(this.getBdb().getBrewery() == null) {
                Log.e("BEER", "Brewery of beer is null.");

            } else {
                this.getBdb().getBrewery().save();
            }

            this.getBdb().save();
        }

        return super.save();
    }

    @Override
    public boolean delete() {
        if (this.getBdb() != null) {
            if(this.getRating() != null) {
                this.getRating().delete();
            }

            if(this.getBdb().getStyle() != null) {
                if(this.getBdb().getStyle().getCategory() != null) {
                    this.getBdb().getStyle().getCategory().delete();
                }

                this.getBdb().getStyle().delete();
            }

            if(this.getBdb().getLabels() != null) {
                this.getBdb().getLabels().delete();
            }

            if(this.getBdb().getAvailability() != null) {
                this.getBdb().getAvailability().delete();
            }

            if(this.getBdb().getSrm() != null) {
                this.getBdb().getSrm().delete();
            }

            if(this.getBdb().getBrewery() != null) {
                this.getBdb().getBrewery().delete();
            }

            this.getBdb().delete();
        }

        return super.delete();
    }
}
