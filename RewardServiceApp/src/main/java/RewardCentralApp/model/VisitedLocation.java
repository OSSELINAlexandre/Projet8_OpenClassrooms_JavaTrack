package RewardCentralApp.model;

import java.util.Date;
import java.util.UUID;

/**
 * <p>The VisitedLocation class represents a visited location with a longitude, a latitude, a date and the ID of a User.</p>
 * <p>This class is shared by all API's.</p>
 * <p>The original library (setting the standard) is available in the GpsUtilApp, and was provided in the project.</p>
 *
 */
public class VisitedLocation {

    public  UUID userId;
    public  Location location;
    public  Date timeVisited;

    public VisitedLocation(UUID userId, Location location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public VisitedLocation() {
    }


}
