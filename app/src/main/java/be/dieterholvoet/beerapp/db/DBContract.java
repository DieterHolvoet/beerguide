package be.dieterholvoet.beerapp.db;

import android.provider.BaseColumns;

/**
 * Created by Dieter on 26/12/2015.
 */
public final class DBContract {

    public DBContract() {}

    public static abstract class BeerEntry implements BaseColumns {
        public static final String TABLE_NAME = "beers";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FOAM = "foam";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_CLARITY = "clarity";
        public static final String COLUMN_NAME_SWEETNESS = "sweetness";
        public static final String COLUMN_NAME_SOURNESS = "sourness";
        public static final String COLUMN_NAME_BITTERNESS = "bitterness";
        public static final String COLUMN_NAME_FULLNESS = "fullness";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_ID = "id";
    }
}
