package be.dieterholvoet.beerguide.model;

/**
 * Created by Dieter on 30/12/2015.
 */
public class BreweryDBResponseLookup {
    private String status;
    private String message;
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
