package be.dieterholvoet.beerguide.rest.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBResponseSearch {
    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
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
