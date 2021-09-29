package GpsUtilApp.model;

/**
 * <p>UserNearbyAttraction is the class that is generated when an attraction is one of the five closest one from a given user's location.</p>
 * <p>it is composed of several attributes :</p>
 * <ul>
 *     <li>The attraction itself.</li>
 *     <li>The location of the user.</li>
 *     <li>The distance between the user's location and the attraction's one.</li>
 *     <li>The rewards points linked to it.</li>
 * </ul>
 *
 */
public class UserNearbyAttraction {

	private Attraction theAttraction;
	private Location userLocation;
	private Double distanceInMilesBetweenUserAndAttraction;
	private int rewardsLinkedToTheAttraction;
	
	public UserNearbyAttraction() {
		super();
	}

	public Attraction getTheAttraction() {
		return theAttraction;
	}

	public void setTheAttraction(Attraction theAttraction) {
		this.theAttraction = theAttraction;
	}

	public Location getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
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
