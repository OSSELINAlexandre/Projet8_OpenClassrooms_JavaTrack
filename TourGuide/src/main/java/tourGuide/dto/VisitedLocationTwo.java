package tourGuide.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationTwo {

    public  UUID userId;
    public  Location location;
    public  Date timeVisited;

    public VisitedLocationTwo(UUID userId, Location location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public VisitedLocationTwo() {
    }
}
