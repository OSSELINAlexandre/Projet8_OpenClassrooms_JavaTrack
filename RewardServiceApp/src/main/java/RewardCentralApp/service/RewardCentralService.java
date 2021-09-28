package RewardCentralApp.service;

import RewardCentralApp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class RewardCentralService {

    private Logger logger = LoggerFactory.getLogger(RewardCentralService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    public static int distanceRequired = 10000;

    private ExecutorService executorService;


    private final RewardCentral rewardCentral;

    public RewardCentralService() {
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attraction, UUID user) {

        return rewardCentral.getAttractionRewardPoints(attraction, user);
    }

    public User calculateRewards(User user) {
        logger.info(("--------------- " + user.getUserName() + " passed for calculateRewards."));
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = user.getAttractions();

        for (VisitedLocation visitedLocation : userLocations) {

            for (Attraction attraction : attractions) {
                /**
                 * Si dans l'ensemble des rewards recu par l'utilisateur l'attraction n'y est
                 * pas, alors / Ok, je pense que le but c'est de verifier par rapport a la
                 * localisation actuelle. Du coup, si une personne a deja visite une
                 * localisation, aucun nouveau point ne lui est donne.
                 */
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

    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > distanceRequired ? false : true;
    }

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
