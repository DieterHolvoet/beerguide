package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 11/01/2016.
 */

@Table(name = "BreweryDBSRMs")
public class BreweryDBSRM extends Model implements Serializable {
    @Column(name = "name")
    String name;

    @Column(name = "hex")
    String hex;

    @Column(name = "BreweryDBBeer", onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBBeer bdbBeer;

    public BreweryDBSRM() {
        super();
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
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
