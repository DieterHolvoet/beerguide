package be.dieterholvoet.beerguide.model;

import be.dieterholvoet.beerguide.R;

/**
 * Created by Dieter on 30/12/2015.
 */

public class RatingElement {
    private String title;
    private String description;
    private int banner;
    private float rating;

    public RatingElement(String title, String description, int banner, float rating) {
        this.banner = banner;
        this.description = description;
        this.rating = rating;
        this.title = title;
    }

    public RatingElement(String title, String description, int banner) {
        this.banner = banner;
        this.description = description;
        this.rating = 2.5f;
        this.title = title;
    }

    public int getBanner() {
        return banner;
    }

    public void setBanner(int banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
