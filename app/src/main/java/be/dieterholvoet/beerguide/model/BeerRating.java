package be.dieterholvoet.beerguide.model;

import com.orm.SugarRecord;

/**
 * Created by Dieter on 8/01/2016.
 */

public class BeerRating extends SugarRecord {
    private int foam;
    private int color;
    private int clarity;
    private int sweetness;
    private int sourness;
    private int bitterness;
    private int fullness;
    private int rating;

    public BeerRating() {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
}
