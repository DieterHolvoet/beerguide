package be.dieterholvoet.beerapp.model;

import java.util.List;

import be.dieterholvoet.beerapp.model.BreweryDBBeer;

/**
 * Created by Dieter on 29/12/2015.
 */

public class SearchResponse {
    private String status;
    private String message;
    private List<BreweryDBBeer> data;

    public SearchResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BreweryDBBeer> getData() {
        return data;
    }

    public void setData(List<BreweryDBBeer> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
