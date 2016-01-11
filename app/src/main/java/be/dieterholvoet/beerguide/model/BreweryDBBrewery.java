package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Dieter on 11/01/2016.
 */
public class BreweryDBBrewery extends SugarRecord implements Serializable {
    private String BreweryDBBreweryID;
    private String descriptions;
    private String name;
    private int established;
    private String website;

    public BreweryDBBrewery() {
    }

    public BreweryDBBrewery(BreweryDBResultBrewery brewery) {
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
