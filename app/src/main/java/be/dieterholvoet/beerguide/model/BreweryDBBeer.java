package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

import be.dieterholvoet.beerguide.rest.model.BreweryDBResultBeer;

/**
 * Created by Dieter on 29/12/2015.
 */

@Table(name = "BreweryDBBeers")
public class BreweryDBBeer extends Model implements Serializable {
    @Column(name = "BreweryDBID")
    private String BreweryDBID;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private String year;

    @Column(name = "labels", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBLabel labels;

    @Column(name = "style", onUpdate = Column.ForeignKeyAction.CASCADE)
    BreweryDBStyle style;

    @Column(name = "availability", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBAvailability availability;

    @Column(name = "srm", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBSRM srm;

    @Column(name = "brewery", onUpdate = Column.ForeignKeyAction.CASCADE)
    BreweryDBBrewery brewery;

    @Column(name = "abv")
    String abv;

    @Column(name = "ibu")
    String ibu;

    @Column(name = "og")
    String og;

    @Column(name = "description")
    String description;

    @Column(name = "Beer", onDelete = Column.ForeignKeyAction.CASCADE)
    private Beer beer;

    public BreweryDBBeer() {
        super();
    }

    public BreweryDBBeer(BreweryDBResultBeer result) {
        super();
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

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
