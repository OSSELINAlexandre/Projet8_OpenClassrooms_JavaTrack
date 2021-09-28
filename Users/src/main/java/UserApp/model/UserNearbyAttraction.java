package UserApp.model;

/**
 * <p>UserNearbyAttraction is a requirement from the client.</p>
 * <p>The class centralizes different information's in order to identify one of the fifth closest attraction of a user.</p>
 * <ul>
 *     <li>The name of the attraction close by.</li>
 *     <li>The location of the User.</li>
 *     <li>The location of the Attraction.</li>
 *     <li>The distance between the attraction and the last visited location of the user that generate this instance.</li>
 *     <li>The rewards point linked to the attraction (if the user visit if; which depends on the API in charge of the rewards, setting the perimeter of acceptable proximity to gather rewards. </li>
 * </ul>
 * <p>Of course, it depends on the last localization of a user.</p>
 *
 */
public class UserNearbyAttraction {

    private String touristAttractionName;
    private Location userLocation;
    private Location attractionLocation;
    private Double distanceInMilesBetweenUserAndAttraction;
    private int rewardsLinkedToTheAttraction;

    public UserNearbyAttraction() {
        super();
    }
    public String getTouristAttractionName() {
        return touristAttractionName;
    }
    public void setTouristAttractionName(String touristAttractionName) {
        this.touristAttractionName = touristAttractionName;
    }
    public Location getUserLocation() {
        return userLocation;
    }
    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }
    public Location getAttractionLocation() {
        return attractionLocation;
    }
    public void setAttractionLocation(Location attractionLocation) {
        this.attractionLocation = attractionLocation;
    }
    public Double getDistanceInMilesBetweenUserAndAttraction() {
        return distanceInMilesBetweenUserAndAttraction;
    }
    public void setDistanceInMilesBetweenUserAndAttraction(Double distanceInMilesBetweenUserAndAttraction) {
        this.distanceInMilesBetweenUserAndAttraction = distanceInMilesBetweenUserAndAttraction;
    }
    public int getRewardsLinkedToTheAttraction() {
        return rewardsLinkedToTheAttraction;
    }
    public void setRewardsLinkedToTheAttraction(int rewardsLinkedToTheAttraction) {
        this.rewardsLinkedToTheAttraction = rewardsLinkedToTheAttraction;
    }


}
