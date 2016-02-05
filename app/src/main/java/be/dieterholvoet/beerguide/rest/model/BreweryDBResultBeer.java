package be.dieterholvoet.beerguide.rest.model;

import be.dieterholvoet.beerguide.model.BreweryDBAvailability;
import be.dieterholvoet.beerguide.model.BreweryDBBrewery;
import be.dieterholvoet.beerguide.model.BreweryDBLabel;
import be.dieterholvoet.beerguide.model.BreweryDBSRM;
import be.dieterholvoet.beerguide.model.BreweryDBStyle;

/**
 * Created by Dieter on 9/01/2016.
 */
public class BreweryDBResultBeer {
    private String id;
    private String name;
    private String year;
    BreweryDBLabel labels;
    BreweryDBStyle style;
    BreweryDBAvailability availability;
    BreweryDBSRM srm;
    BreweryDBBrewery brewery;
    String abv;
    String ibu;
    String og;
    String description;

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

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public BreweryDBAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(BreweryDBAvailability availability) {
        this.availability = availability;
    }

    public BreweryDBBrewery getBrewery() {
        return brewery;
    }

    public void setBrewery(BreweryDBBrewery brewery) {
        this.brewery = brewery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getOg() {
        return og;
    }

    public void setOg(String og) {
        this.og = og;
    }

    public BreweryDBSRM getSrm() {
        return srm;
    }

    public void setSrm(BreweryDBSRM srm) {
        this.srm = srm;
    }
}
