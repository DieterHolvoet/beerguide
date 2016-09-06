package be.dieterholvoet.beerguide.rest.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Dieter on 11/01/2016.
 */
public class BreweryDBResultBrewery {
    @Expose
    private String id;
    @Expose
    private String descriptions;
    @Expose
    private String name;
    @Expose
    private int established;
    @Expose
    private String website;

    public BreweryDBResultBrewery() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
