package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBBeer extends SugarRecord {
    private String BreweryDBID;
    private Beer beer;
    private String name;
    private String year;
    BreweryDBLabel labels;
    BreweryDBStyle style;

    public BreweryDBBeer() {
        this.labels = new BreweryDBLabel();
        this.style = new BreweryDBStyle();
    }

    public BreweryDBBeer(String BreweryDBID, String name) {
        this.BreweryDBID = BreweryDBID;
        this.name = name;
        this.labels = new BreweryDBLabel();
        this.style = new BreweryDBStyle();
    }

    public BreweryDBBeer(BreweryDBResultBeer result) {
        this.BreweryDBID = result.getId();
        this.name = result.getName();
        this.year = result.getYear();
        this.labels = result.getLabels();
        this.style = result.getStyle();
    }

    @Override
    public String toString() {
        return name;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public String getBreweryDBID() {
        return BreweryDBID;
    }

    public void setBreweryDBID(String breweryDBID) {
        BreweryDBID = breweryDBID;
    }

    public BreweryDBLabel getLabels() {
        return labels;
    }

    public void setLabels(BreweryDBLabel labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
