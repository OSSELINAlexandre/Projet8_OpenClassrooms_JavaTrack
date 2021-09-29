package RewardCentralApp.model;

/**
 *
 * <p>The Location class represents a location with its latitude and longitude.</p>
 *
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
