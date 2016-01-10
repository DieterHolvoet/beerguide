package be.dieterholvoet.beerguide.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BeerRating;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;

/**
 * Created by Dieter on 8/01/2016.
 */
public class DB {
    /*protected static DB INSTANCE;
    private static DBHandler handler;
    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    public static DB getInstance() {
        return INSTANCE;
    }

    public static void setInstance(Context context) {
        DB.INSTANCE = new DB(context);
    }

    public DB(Context context) {
        this.handler = new DBHandler(context, null, null, 1);
        this.readableDB = this.handler.getReadableDatabase();
        this.writableDB = this.handler.getWritableDatabase();
    }

    public long addBeer(Beer beer) {
        ContentValues values = new ContentValues();
        BeerRating rating = beer.getRating();
        BreweryDBBeer bdb = beer.getBdb();

        if(rating != null) {
            addRating(rating);
        }

        if(bdb != null) {
            addBreweryDBBeer(bdb);
        }
        
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(Beer.Contract.COLUMN_NAME_BDB_ID, beer.getBdb_id());
        values.put(Beer.Contract.COLUMN_NAME_NAME, beer.getName());
        values.put(Beer.Contract.COLUMN_NAME_DATE_ADDED, dateFormat.format(now));
        


        return writableDB.insert(DBContract.BeerEntry.TABLE_NAME, null, values);
    }

    public long addRating(BeerRating rating) {
        ContentValues values = new ContentValues();

        values.put(BeerRating.Contract.COLUMN_NAME_RATING, rating.getRating());
        values.put(BeerRating.Contract.COLUMN_NAME_BITTERNESS, rating.getBitterness());
        values.put(BeerRating.Contract.COLUMN_NAME_CLARITY, rating.getClarity());
        values.put(BeerRating.Contract.COLUMN_NAME_COLOR, rating.getColor());
        values.put(BeerRating.Contract.COLUMN_NAME_FOAM, rating.getFoam());
        values.put(BeerRating.Contract.COLUMN_NAME_FULLNESS, rating.getFullness());
        values.put(BeerRating.Contract.COLUMN_NAME_SOURNESS, rating.getSourness());
        values.put(BeerRating.Contract.COLUMN_NAME_SWEETNESS, rating.getSweetness());

        return writableDB.insert(BeerRating.Contract.TABLE_NAME, null, values);
    }

    public long addBreweryDBBeer(BreweryDBBeer bdb) {
        ContentValues values = new ContentValues();

        values.put(BreweryDBBeer.Contract.COLUMN_NAME_NAME, bdb.getName());
        values.put(BreweryDBBeer.Contract.COLUMN_NAME_YEAR, bdb.getYear());
        values.put(BreweryDBBeer.Contract.COLUMN_NAME_LABEL_ID, bdb.getLabels().getID());
        values.put(BreweryDBBeer.Contract.COLUMN_NAME_STYLE_ID, bdb.getStyle().getID());

        return writableDB.insert(BreweryDBBeer.Contract.TABLE_NAME, null, values);
    }

    public ArrayList<Beer> getBeers() {
        ArrayList<Beer> beers = new ArrayList<>();
        Cursor cursor = readableDB.query(DBContract.BeerEntry.TABLE_NAME, DBContract.BeerEntry.TABLE_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Beer beer = cursorToBeer(cursor);
            beers.add(beer);
            cursor.moveToNext();
        }
        cursor.close();

        return beers;
    }

    public ArrayList<Beer> getRecentBeers(int daysCount, int resultCount) {
        ArrayList<Beer> beers = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyyddMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysCount);

        Cursor cursor = readableDB.query(
                DBContract.BeerEntry.TABLE_NAME,
                DBContract.BeerEntry.TABLE_COLUMNS,
                DBContract.BeerEntry.COLUMN_NAME_DATE_ADDED + " >= " + df.format(cal),
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            beers.add(cursorToBeer(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return beers;
    }

    public boolean deleteBeer(String productname) {
        boolean result = false;
        String query = "Select * FROM " + DBContract.BeerEntry.TABLE_NAME + " WHERE " +
                DBContract.BeerEntry.COLUMN_NAME_NAME + " =  \"" + productname + "\"";

        Cursor cursor = writableDB.rawQuery(query, null);
        Beer beer = new Beer();

        if (cursor.moveToFirst()) {
            beer.set_id(Integer.parseInt(cursor.getString(0)));
            writableDB.delete(DBContract.BeerEntry.TABLE_NAME, DBContract.BeerEntry.COLUMN_NAME_ID + " = ?",
                    new String[] { String.valueOf(beer.get_id()) });
            cursor.close();
            result = true;
        }

        return result;
    }

    private Beer cursorToBeer(Cursor c) {
        return new Beer(
                c.getInt(0),    // _ID
                c.getInt(1),    // BREWERY_DB_ID
                c.getInt(2),    // COLUMN_NAME_BITTERNESS
                c.getInt(3),    // COLUMN_NAME_CLARITY
                c.getInt(4),    // COLUMN_NAME_COLOR
                c.getInt(5),    // COLUMN_NAME_FOAM
                c.getInt(6),    // COLUMN_NAME_FULLNESS
                c.getString(7), // COLUMN_NAME_NAME
                c.getInt(8),    // COLUMN_NAME_RATING
                c.getInt(9),    // COLUMN_NAME_SOURNESS
                c.getInt(10));  // COLUMN_NAME_SWEETNESS
    }*/
}
