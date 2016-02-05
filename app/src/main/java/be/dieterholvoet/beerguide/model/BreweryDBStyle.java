package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 8/01/2016.
 */

@Table(name = "BreweryDBStyles")
public class BreweryDBStyle extends Model implements Serializable {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category", onUpdate = Column.ForeignKeyAction.CASCADE)
    private BreweryDBCategory category;

    public BreweryDBStyle() {
        super();
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
