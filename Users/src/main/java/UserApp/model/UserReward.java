package UserApp.model;


/**
 * <p>UserReward is the class representing the rewards.</p>
 * <p>In the User class, we have a list of this type for gathered rewards.</p>
 * <p>As we have said before, the rewards depends on the proximity policy available in the API in charge of the rewards (in our case RewardApp).</p>
 *<p>It is composed as follow :</p>
 * <ul>
 *     <li>The visited location that gather the reward.<li>
 *     <li>The attraction that generated the reward.</li>
 *     <li>The points of rewards for it.</li>
 * </ul>
 */
public class UserReward {

	public  VisitedLocation visitedLocation;
	public  Attraction attraction;
	private int rewardPoints;

	public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public UserReward() {
	}

	public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
