package be.dieterholvoet.beerguide.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dieter on 8/01/2016.
 */

@Table(name = "Ratings")
public class BeerRating extends Model implements Serializable {
    @Column(name = "foam")
    private int foam;

    @Column(name = "color")
    private int color;

    @Column(name = "clarity")
    private int clarity;

    @Column(name = "sweetness")
    private int sweetness;

    @Column(name = "sourness")
    private int sourness;

    @Column(name = "bitterness")
    private int bitterness;

    @Column(name = "fullness")
    private int fullness;

    @Column(name = "rating")
    private float rating;

    @Column(name = "Beer", onDelete = Column.ForeignKeyAction.CASCADE)
    private Beer beer;

    public BeerRating() {
        super();
    }

    public int getBitterness() {
        return bitterness;
    }

    public void setBitterness(int bitterness) {
        this.bitterness = bitterness;
    }

    public int getClarity() {
        return clarity;
    }

    public void setClarity(int clarity) {
        this.clarity = clarity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getFoam() {
        return foam;
    }

    public void setFoam(int foam) {
        this.foam = foam;
    }

    public int getFullness() {
        return fullness;
    }

    public void setFullness(int fullness) {
        this.fullness = fullness;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSourness() {
        return sourness;
    }

    public void setSourness(int sourness) {
        this.sourness = sourness;
    }

    public int getSweetness() {
        return sweetness;
    }

    public void setSweetness(int sweetness) {
        this.sweetness = sweetness;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
