package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 11/01/2016.
 */

@Table(name = "BreweryDBAvailabilities")
public class BreweryDBAvailability extends Model implements Serializable {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "BreweryDBBeer", onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBBeer bdbBeer;

    public BreweryDBAvailability() {
        super();
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

    public BreweryDBBeer getBdbBeer() {
        return bdbBeer;
    }

    public void setBdbBeer(BreweryDBBeer bdbBeer) {
        this.bdbBeer = bdbBeer;
    }
}
