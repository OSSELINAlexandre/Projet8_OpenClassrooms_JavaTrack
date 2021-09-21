package UserApp.dto;

import UserApp.model.Provider;
import UserApp.model.UserReward;
import UserApp.model.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class UserDTO {


    private Logger logger = LoggerFactory.getLogger(UserDTO.class);
    private UUID userId;
    private  String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private CopyOnWriteArrayList<VisitedLocation> visitedLocations = new  CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<UserReward> userRewards = new  CopyOnWriteArrayList<>();
    private UserPreferencesDTO userPreferences = new UserPreferencesDTO();
    private List<Provider> tripDeals = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();


    public UserDTO(UUID userId, String userName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UserDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public CopyOnWriteArrayList<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(CopyOnWriteArrayList<VisitedLocation> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }

    public CopyOnWriteArrayList<UserReward> getUserRewards() {
        return userRewards;
    }

    public void setUserRewards(CopyOnWriteArrayList<UserReward> userRewards) {
        this.userRewards = userRewards;
    }

    public UserPreferencesDTO getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferencesDTO userPreferences) {
        this.userPreferences = userPreferences;
    }

    public List<Provider> getTripDeals() {
        return tripDeals;
    }

    public void setTripDeals(List<Provider> tripDeals) {
        this.tripDeals = tripDeals;
    }

    public void addUserReward(UserReward userReward) {
        if(userRewards.stream().filter(r -> r.attraction.attractionName.equals(userReward.attraction.attractionName)).count() == 0) {
            logger.info("pretty sure it never goes even there man ! :)");
            userRewards.add(userReward);
        }
    }

}
