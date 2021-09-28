package UserApp.model;

import java.util.UUID;

/**
 * <p>The Attraction class represents an attraction.</p>
 * <p>This class is shared by all API's.</p>
 * <p>The original library (setting the standard) is available in the GpsUtilApp, and was provided in the project.</p>
 *
 */
public class Attraction extends Location{

    public  String attractionName;
    public  String city;
    public  String state;
    public  UUID attractionId;

    public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    public Attraction(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public Attraction() {
    }
}
