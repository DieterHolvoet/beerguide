package be.dieterholvoet.beerapp.rest;

import java.util.List;

/**
 * Created by Dieter on 29/12/2015.
 */

public class SearchResponse {
    private String status;
    private String message;
    private List<BreweryDBResult> data;

    public SearchResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BreweryDBResult> getData() {
        return data;
    }

    public void setData(List<BreweryDBResult> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
