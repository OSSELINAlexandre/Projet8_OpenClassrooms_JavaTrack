package RewardCentralApp.service;

import RewardCentralApp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * <p>RewardCentralService is the service in charge of the business logic, making the link between the client (via the controller) and the library by providing added value.</p>
 * <p>The library is present for building the whole structure of the microservices of this project : once the application is validated, we can simply communicate with an online API instead of a library.</p>
 *
 * <p>In the present class, the functionalities of the library are available through the attribute RewardCentral.</p>
 *
 */
@Service
public class RewardCentralService {

    private Logger logger = LoggerFactory.getLogger(RewardCentralService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    public static int distanceRequired = 10;

    private ExecutorService executorService;


    private final RewardCentral rewardCentral;

    public RewardCentralService() {
        this.rewardCentral = new RewardCentral();
    }

    /**
     * <p>getAttractionRewardPoints call the library rewardCentral and return an amount of reward for a given attraction and a given user.</p>
     *
     *
     * @param attraction
     *              The attraction from which we want to calculate points.
     * @param user
     *              The User to whom we want to calculate points.
     * @return
     */
    public int getAttractionRewardPoints(UUID attraction, UUID user) {

        return rewardCentral.getAttractionRewardPoints(attraction, user);
    }

    /**
     * <p>calculateRewards take a user as an argument.</p>
     * <p>From this user, it takes the list of all attractions from which we want to see if a reward is applicable.</p>
     * <p>For each visited location of the user, and for each attraction available, the method will check if the occurrence of the visited location is within a distance
     * that recompense with a reward.</p>
     * <p>The proximity in which the reward can be attributed is set by the distanceRequired attributes.</p>
     * <p>If a reward has already been added (see stream().count() )to the user for a given attraction, a new reward for this attraction will not be given.</p>
     *
     * @param user
     *          The user from whom we want to calculate the rewards.
     * @return
     */
    public User calculateRewards(User user) {
        logger.info(("--------------- " + user.getUserName() + " passed for calculateRewards."));
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = user.getAttractions();

        for (VisitedLocation visitedLocation : userLocations) {

            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream()
                        .filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, rewardCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId())));

                    }
                }
            }
        }

        return user;

    }

    /**
     * <p>nearAttraction is a utility method, it verifies is two location points are within a proximity defined by the attribute 'distanceRequired'.</p>
     * <p>It returns true if the distance is inferior to the distanceRequired, and false otherwise.</p>
     *
     * @param visitedLocation
     * @param attraction
     * @return
     */
    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > distanceRequired ? false : true;
    }

    /**
     *
     * <p>getDistance is a utility function that calculates the distance between two points.</p>
     *
     * @param loc1
     * @param loc2
     * @return
     */
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

    /**
     *
     * <p>calculateAllTheRewards is a 'principal' method of our whole system.</p>
     * <p>Indeed, it takes a list of user as an argument, create a fixedThreadPool to split the calculus between different thread.</p>
     * <p>It was required to do so in order to pass the performance requirement set by the clients.</p>
     * <p>Each thread will call the calculateRewards() methods and return a result, in the future (which is why we have a CompletableFuture).</p>
     * <p>Once all the results are gathered, we shut down the executorService and the list of users with the rewards calculus done is sent back to the requester.</p>
     *
     * @param theUsers
     *              The list of users from which we want to calculate the rewards.
     * @return List<User>
     */
    public List<User> calculateAllTheRewards(List<User> theUsers) {

        executorService = Executors.newFixedThreadPool(1500);
        CopyOnWriteArrayList<User> theResult = new CopyOnWriteArrayList<>();

        for(User u : theUsers){
            CompletableFuture.supplyAsync( () -> theResult.add(calculateRewards(u)), executorService );
        }

        executorService.shutdown();

        try {
            executorService.shutdown();
            executorService.awaitTermination(15, TimeUnit.MINUTES);
        }catch(Exception e){
            executorService.shutdown();
        }

        logger.info("In rewardApp, the size of the list sent is : " + theResult.size());


        return theResult;

    }
}
