package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Dieter on 11/01/2016.
 */
public class BreweryDBAvailability extends SugarRecord implements Serializable {
    private String name;
    private String description;

    public BreweryDBAvailability() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
