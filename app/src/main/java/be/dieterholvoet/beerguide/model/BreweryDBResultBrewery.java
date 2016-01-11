package be.dieterholvoet.beerguide.model;

/**
 * Created by Dieter on 11/01/2016.
 */
public class BreweryDBResultBrewery {
    private String id;
    private String descriptions;
    private String name;
    private int established;
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
