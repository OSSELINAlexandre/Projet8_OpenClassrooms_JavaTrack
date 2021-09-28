package UserApp.model;

import java.util.UUID;
/**
 * <p>The Provider class represents a provider of trip (a travel agency for our application).</p>
 * <p>This class is shared by all API's.</p>
 * <p>The original library (setting the standard) is available in the TripApp, and was provided in the project.</p>
 *
 */
public class Provider {

    public String name;
    public double price;
    public UUID tripId;

    public Provider(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }

    public Provider() {
    }

}
