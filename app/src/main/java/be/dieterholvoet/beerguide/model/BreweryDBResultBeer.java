package be.dieterholvoet.beerguide.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BreweryDBResultBeer {
    private String id;
    private String name;
    private String year;
    BreweryDBLabel labels;
    BreweryDBStyle style;

    public BreweryDBResultBeer() {
    }

    @Override
    public String toString() {
        return name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
