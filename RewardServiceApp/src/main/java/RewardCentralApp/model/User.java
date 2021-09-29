package RewardCentralApp.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>The User class represent the user in our system. As we have seen in the User App (the client application that runs the whole logic)
 * , this User class is stripped of useless information's for this application.
 * </p>
 * <p>The equivalent type in our UserApp is the UserGpsDTO.</p>
 *
 * <p>This class does not instantiate any of its attributes because all the attributes are instantiated in the User Application</p>
 *
 * <p>Compared to the User type in the UserApp, we added as an attribute the list of all attractions. We did it in order for the rewardApp to stay autonomous, and so that the
 * application does not need to call another microservices in order to function properly. This attribute therefore reduce the coupling between the application, make it more reliable.</p>
 */
public class User {
	private Logger logger = LoggerFactory.getLogger(User.class);

	private UUID userId;
	private String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private CopyOnWriteArrayList<VisitedLocation> visitedLocations;
	private CopyOnWriteArrayList<UserReward> userRewards;
	private List<Attraction> attractions;
	
	
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public User() {
	}

	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void setUserRewards(CopyOnWriteArrayList<UserReward> userRewards) {
		this.userRewards = userRewards;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	
	public  CopyOnWriteArrayList<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void setVisitedLocations(CopyOnWriteArrayList<VisitedLocation> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}

	public void clearVisitedLocations() {
		visitedLocations.clear();
	}
	
	public void addUserReward(UserReward userReward) {
		if(userRewards.stream().filter(r -> r.attraction.attractionName.equals(userReward.attraction.attractionName)).count() == 0) {
			logger.info(" A Reward has been added to : " + this.getUserName() +" --- ");
			userRewards.add(userReward);
		}
	}
	
	public  CopyOnWriteArrayList<UserReward> getUserRewards() {
		return userRewards;
	}

	public VisitedLocation TheLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}

	public List<Attraction> getAttractions() {
		return attractions;
	}

	public void setAttractions(List<Attraction> attractions) {
		this.attractions = attractions;
	}
}
