package be.dieterholvoet.beerapp.model;

import android.provider.BaseColumns;

/**
 * Created by Dieter on 8/01/2016.
 */
public class BeerRating {
    private int _id;
    private int foam;
    private int color;
    private int clarity;
    private int sweetness;
    private int sourness;
    private int bitterness;
    private int fullness;
    private int rating;

    public BeerRating() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getBitterness() {
        return bitterness;
    }

    public void setBitterness(int bitterness) {
        this.bitterness = bitterness;
    }

    public int getClarity() {
        return clarity;
    }

    public void setClarity(int clarity) {
        this.clarity = clarity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getFoam() {
        return foam;
    }

    public void setFoam(int foam) {
        this.foam = foam;
    }

    public int getFullness() {
        return fullness;
    }

    public void setFullness(int fullness) {
        this.fullness = fullness;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSourness() {
        return sourness;
    }

    public void setSourness(int sourness) {
        this.sourness = sourness;
    }

    public int getSweetness() {
        return sweetness;
    }

    public void setSweetness(int sweetness) {
        this.sweetness = sweetness;
    }

    public static abstract class Contract implements BaseColumns {
        public static final String TABLE_NAME = "ratings";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FOAM = "foam";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_CLARITY = "clarity";
        public static final String COLUMN_NAME_SWEETNESS = "sweetness";
        public static final String COLUMN_NAME_SOURNESS = "sourness";
        public static final String COLUMN_NAME_BITTERNESS = "bitterness";
        public static final String COLUMN_NAME_FULLNESS = "fullness";
        public static final String COLUMN_NAME_RATING = "rating";

        public static final String[] TABLE_COLUMNS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_FOAM,
                COLUMN_NAME_COLOR,
                COLUMN_NAME_CLARITY,
                COLUMN_NAME_SWEETNESS,
                COLUMN_NAME_SOURNESS,
                COLUMN_NAME_BITTERNESS,
                COLUMN_NAME_FULLNESS,
                COLUMN_NAME_RATING
        };
    }
}
