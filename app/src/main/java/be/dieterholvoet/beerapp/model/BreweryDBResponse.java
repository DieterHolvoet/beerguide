package be.dieterholvoet.beerapp.model;

import be.dieterholvoet.beerapp.model.BreweryDBBeer;

/**
 * Created by Dieter on 30/12/2015.
 */
public class BreweryDBResponse {
    private String status;
    private String message;
    private BreweryDBBeer data;

    public BreweryDBBeer getData() {
        return data;
    }

    public void setData(BreweryDBBeer data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
