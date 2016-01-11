package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBBeer extends SugarRecord implements Serializable {
    private String BreweryDBID;
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
        this.availability = result.getAvailability();
        this.srm = result.getSrm();
        this.brewery = result.getBrewery();
        this.abv = result.getAbv();
        this.ibu = result.getIbu();
        this.og = result.getOg();
        this.description = result.getDescription();
    }

    @Override
    public String toString() {
        return name;
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
}
