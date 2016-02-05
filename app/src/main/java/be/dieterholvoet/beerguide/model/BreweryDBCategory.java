package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 8/01/2016.
 */

@Table(name = "BreweryDBCategories")
public class BreweryDBCategory extends Model implements Serializable {
    @Column(name = "name")
    private String name;

    public BreweryDBCategory() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
