package RewardCentralApp.model;

import java.util.UUID;

/**
 * <p>The Attraction class represent an attraction in our system. It's a copy of the type available in gpsUtil.jar.</p>
 * <p>We needed to copy the type for standardization purposes in the whole system.</p>
 * <p>An Attraction is represented by the following traits.</p>
 * <ul>
 *     <li>The name of the attraction.</li>
 *     <li>The city of the attraction.</li>
 *     <li>The state of the attraction.</li>
 *     <li>The ID of the attraction.</li>
 * </ul>
 *
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
