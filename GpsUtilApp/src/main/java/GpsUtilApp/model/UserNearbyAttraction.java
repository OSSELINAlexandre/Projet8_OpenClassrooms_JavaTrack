package GpsUtilApp.model;




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
