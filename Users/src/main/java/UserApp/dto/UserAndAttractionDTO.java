package UserApp.dto;

import UserApp.model.Attraction;
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

/**
 *
 * <p>UserAndAttractionDTO is the type corresponding to the API in charge if the rewards (in our case, RewardApp). </p>
 * <p>It centralizes less information's than User in order to avoid useless transfer of information's.</p>
 * <p>The type also has an additional attribute (attractions) to facilitate the independence of the API. </p>
 * <p>Indeed, the API can be completely autonomous and stateless if the DTO itself provided the list of attractions (the Reward API does not need to communicate with the API in charge of the attractions.)</p>
 *
 */
public class UserAndAttractionDTO{
    private Logger logger = LoggerFactory.getLogger(UserAndAttractionDTO.class);
    private ReentrantLock lock = new ReentrantLock();

    private UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private CopyOnWriteArrayList<VisitedLocation> visitedLocations = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<UserReward> userRewards = new  CopyOnWriteArrayList<>();
    private List<Attraction> attractions = new ArrayList<>();

    public UserAndAttractionDTO() {
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

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
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


}
