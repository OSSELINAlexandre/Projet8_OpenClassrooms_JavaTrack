package GpsUtilApp.model;

import java.util.Date;
import java.util.UUID;

/**
 * <p>The VisitedLocation class represents a visited location by a specific user.</p>
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
