package be.dieterholvoet.beerapp.rest;

/**
 * Created by Dieter on 29/12/2015.
 */
public class Label {
    private String medium;
    private String large;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
