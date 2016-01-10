package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

/**
 * Created by Dieter on 8/01/2016.
 */
public class BreweryDBCategory extends SugarRecord {
    private String name;

    public BreweryDBCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
