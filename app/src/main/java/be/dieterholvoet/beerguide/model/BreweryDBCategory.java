package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Dieter on 8/01/2016.
 */
public class BreweryDBCategory extends SugarRecord implements Serializable {
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
