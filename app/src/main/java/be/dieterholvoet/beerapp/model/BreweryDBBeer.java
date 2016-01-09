package be.dieterholvoet.beerapp.model;

import android.provider.BaseColumns;

/**
 * Created by Dieter on 29/12/2015.
 */
public class BreweryDBBeer {
    private String id;
    private String name;
    private String year;
    private BreweryDBLabel labels;
    private BreweryDBStyle style;

    public BreweryDBBeer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BreweryDBLabel getLabels() {
        return labels;
    }

    public void setLabels(BreweryDBLabel labels) {
        this.labels = labels;
    }

    public BreweryDBStyle getStyle() {
        return style;
    }

    public void setStyle(BreweryDBStyle style) {
        this.style = style;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return name;
    }

    public static abstract class Contract implements BaseColumns {
        public static final String TABLE_NAME = "brewerydb";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LABEL_ID = "label_id";
        public static final String COLUMN_NAME_STYLE_ID = "style_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_YEAR = "year";

        public static final String[] TABLE_COLUMNS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_LABEL_ID,
                COLUMN_NAME_STYLE_ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_YEAR
        };
    }
}
