package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

import be.dieterholvoet.beerguide.rest.model.BreweryDBResultBrewery;

/**
 * Created by Dieter on 11/01/2016.
 */

@Table(name = "BreweryDBBreweries")
public class BreweryDBBrewery extends Model implements Serializable {
    @Column(name = "BreweryDBBreweryID")
    private String BreweryDBBreweryID;

    @Column(name = "descriptions")
    private String descriptions;

    @Column(name = "name")
    private String name;

    @Column(name = "established")
    private int established;

    @Column(name = "website")
    private String website;

    public BreweryDBBrewery() {
        super();
    }

    public BreweryDBBrewery(BreweryDBResultBrewery brewery) {
        super();
        this.BreweryDBBreweryID = brewery.getId();
        this.descriptions = brewery.getDescriptions();
        this.name = brewery.getName();
        this.established = brewery.getEstablished();
        this.website = brewery.getWebsite();
    }

    public String getBreweryDBBreweryID() {
        return BreweryDBBreweryID;
    }

    public void setBreweryDBBreweryID(String breweryDBBreweryID) {
        BreweryDBBreweryID = breweryDBBreweryID;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getEstablished() {
        return established;
    }

    public void setEstablished(int established) {
        this.established = established;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
