package be.dieterholvoet.beerapp.model;

/**
 * Created by Dieter on 8/01/2016.
 */
public class BreweryDBStyle {
    private int id;
    private String name;
    private String description;
    private BreweryDBCategory category;

    public BreweryDBStyle() {
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

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
