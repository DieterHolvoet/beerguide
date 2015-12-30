package be.dieterholvoet.beerapp.rest;

/**
 * Created by Dieter on 29/12/2015.
 */
public class BreweryDBResult {
    private String id;
    private String name;
    private Label labels;

    public BreweryDBResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getLabels() {
        return labels;
    }

    public void setLabels(Label labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return name;
    }
}
