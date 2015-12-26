package be.dieterholvoet.beerapp.db;

/**
 * Created by Dieter on 26/12/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.dieterholvoet.beerapp.model.Beer;

/**
 * Created by Dieter on 15/11/2015.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "beers.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.BeerEntry.TABLE_NAME + "("
            + DBContract.BeerEntry._ID + " INTEGER PRIMARY KEY," + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_BITTERNESS + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_CLARITY + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_COLOR + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_FOAM + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_FULLNESS + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_RATING + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_SOURNESS + INTEGER_TYPE + COMMA_SEP
            + DBContract.BeerEntry.COLUMN_NAME_SWEETNESS + INTEGER_TYPE + COMMA_SEP
            + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.BeerEntry.TABLE_NAME;

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addBeer(Beer beer) {
        ContentValues values = new ContentValues();
        values.put(DBContract.BeerEntry.COLUMN_NAME_NAME, beer.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(DBContract.BeerEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Beer findProduct(String productname) {
        String query = "Select * FROM " + DBContract.BeerEntry.TABLE_NAME + " WHERE " +
                DBContract.BeerEntry.COLUMN_NAME_NAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Beer beer = new Beer();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            beer.set_id(Integer.parseInt(cursor.getString(0)));
            beer.setName(cursor.getString(1));
            cursor.close();

        } else {
            beer = null;
        }

        db.close();
        return beer;
    }

    public boolean deleteBeer(String productname) {
        boolean result = false;
        String query = "Select * FROM " + DBContract.BeerEntry.TABLE_NAME + " WHERE " +
                DBContract.BeerEntry.COLUMN_NAME_NAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Beer beer = new Beer();

        if (cursor.moveToFirst()) {
            beer.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(DBContract.BeerEntry.TABLE_NAME, DBContract.BeerEntry.COLUMN_NAME_ID + " = ?",
                    new String[] { String.valueOf(beer.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
