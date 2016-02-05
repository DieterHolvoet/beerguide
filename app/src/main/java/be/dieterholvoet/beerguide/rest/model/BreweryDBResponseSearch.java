package be.dieterholvoet.beerguide.rest.model;

import java.util.List;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBResponseSearch {
    private String status;
    private String message;
    private List<BreweryDBResultBeer> data;

    public BreweryDBResponseSearch() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BreweryDBResultBeer> getData() {
        return data;
    }

    public void setData(List<BreweryDBResultBeer> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
