package be.dieterholvoet.beerapp.rest;

/**
 * Created by Dieter on 30/12/2015.
 */
public class BeerResponse {
    private String status;
    private String message;
    private BreweryDBResult data;

    public BreweryDBResult getData() {
        return data;
    }

    public void setData(BreweryDBResult data) {
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
