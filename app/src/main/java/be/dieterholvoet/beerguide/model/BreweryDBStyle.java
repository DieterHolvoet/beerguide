package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Dieter on 8/01/2016.
 */
public class BreweryDBStyle extends SugarRecord implements Serializable {
    private String name;
    private String description;
    private BreweryDBCategory category;

    public BreweryDBStyle() {
        this.category = new BreweryDBCategory();
    }

    public BreweryDBCategory getCategory() {
        return category;
    }

    public void setCategory(BreweryDBCategory category) {
        this.category = category;
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
