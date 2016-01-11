package be.dieterholvoet.beerguide.model;

/**
 * Created by Dieter on 11/01/2016.
 */
public class BreweryDBResponseBrewery {
    private String status;
    private String message;
    private BreweryDBResultBrewery data;

    public BreweryDBResultBrewery getData() {
        return data;
    }

    public void setData(BreweryDBResultBrewery data) {
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
