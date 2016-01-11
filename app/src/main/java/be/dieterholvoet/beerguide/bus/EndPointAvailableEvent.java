package be.dieterholvoet.beerguide.bus;

/**
 * Created by Dieter on 10/01/2016.
 */
public class EndPointAvailableEvent {
    boolean available;

    public EndPointAvailableEvent(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
