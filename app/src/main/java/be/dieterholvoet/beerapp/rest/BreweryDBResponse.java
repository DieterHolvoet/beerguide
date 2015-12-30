package be.dieterholvoet.beerapp.rest;

import java.util.List;

/**
 * Created by Dieter on 29/12/2015.
 */

public class BreweryDBResponse {
    private List<BreweryDBResult> data;

    public BreweryDBResponse() {
    }

    public List<BreweryDBResult> getData() {
        return data;
    }

    public void setData(List<BreweryDBResult> data) {
        this.data = data;
    }
}
