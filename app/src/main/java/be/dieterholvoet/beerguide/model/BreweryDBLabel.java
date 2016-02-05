package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 29/12/2015.
 */

@Table(name = "BreweryDBLabels")
public class BreweryDBLabel extends Model implements Serializable {
    @Column(name = "medium")
    private String medium;

    @Column(name = "large")
    private String large;

    @Column(name = "icon")
    private String icon;

    @Column(name = "BreweryDBBeer", onDelete = Column.ForeignKeyAction.CASCADE)
    BreweryDBBeer bdbBeer;

    public BreweryDBLabel() {
        super();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public BreweryDBBeer getBdbBeer() {
        return bdbBeer;
    }

    public void setBdbBeer(BreweryDBBeer bdbBeer) {
        this.bdbBeer = bdbBeer;
    }
}
