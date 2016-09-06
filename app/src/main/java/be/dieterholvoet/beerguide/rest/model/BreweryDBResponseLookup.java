package be.dieterholvoet.beerguide.rest.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Dieter on 30/12/2015.
 */
public class BreweryDBResponseLookup {
    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
    private BreweryDBResultBeer data;

    public BreweryDBResultBeer getData() {
        return data;
    }

    public void setData(BreweryDBResultBeer data) {
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
