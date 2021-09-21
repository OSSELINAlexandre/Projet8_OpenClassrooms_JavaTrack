package UserApp.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class User {
	private Logger logger = LoggerFactory.getLogger(User.class);
	private UUID userId;
	private String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private CopyOnWriteArrayList<VisitedLocation> visitedLocations = new  CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<UserReward> userRewards = new  CopyOnWriteArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();
	private ReentrantLock lock = new ReentrantLock();
	
	
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
	
	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	
	public  CopyOnWriteArrayList<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}
	
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}
	
	public void addUserReward(UserReward userReward) {
		if(userRewards.stream().filter(r -> r.attraction.attractionName.equals(userReward.attraction.attractionName)).count() == 0) {
			logger.info("pretty sure it never goes even there man ! :)");
			userRewards.add(userReward);
		}
	}
	
	public  CopyOnWriteArrayList<UserReward> getUserRewards() {
		return userRewards;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<Provider> getTripDeals() {
		return tripDeals;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVisitedLocations(CopyOnWriteArrayList<VisitedLocation> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}


	public ReentrantLock getLock() {
		return lock;
	}

	public void setLock(ReentrantLock lock) {
		this.lock = lock;
	}

	public void setUserRewards(CopyOnWriteArrayList<UserReward> userRewards) {
		this.userRewards = userRewards;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", latestLocationTimestamp=" + latestLocationTimestamp +
				", visitedLocations=" + visitedLocations +
				", userRewards=" + userRewards +
				", userPreferences=" + userPreferences +
				", tripDeals=" + tripDeals +
				", lock=" + lock +
				'}';
	}
}
