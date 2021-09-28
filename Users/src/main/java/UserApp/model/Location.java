package UserApp.model;

/**
 * <p>The Location class represents a location with a longitude and a latitude.</p>
 * <p>This class is shared by all API's.</p>
 * <p>The original library (setting the standard) is available in the GpsUtilApp, and was provided in the project.</p>
 *
 */
public class Location {

    public  double longitude;
    public  double latitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }
}
