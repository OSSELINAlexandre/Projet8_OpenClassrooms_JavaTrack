package RewardCentralApp.service;

import RewardCentralApp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.UUID;

@Service
public class RewardCentralService {

    private Logger logger = LoggerFactory.getLogger(RewardCentralService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private static int distanceRequired = Integer.MAX_VALUE;


    private final RewardCentral rewardCentral;

    public RewardCentralService() {
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attraction, UUID user) {

        return rewardCentral.getAttractionRewardPoints(attraction, user);
    }

    public User calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = user.getAttractions();

        logger.info("============= HERE OR NOT ? " + user.getUserName());
        for (VisitedLocation visitedLocation : userLocations) {

            for (Attraction attraction : attractions) {
                /**
                 * Si dans l'ensemble des rewards recu par l'utilisateur l'attraction n'y est
                 * pas, alors / Ok, je pense que le but c'est de verifier par rapport a la
                 * localisation actuelle. Du coup, si une personne a deja visite une
                 * localisation, aucun nouveau point ne lui est donne.
                 */
                if (user.getUserRewards().stream().parallel()
                        .filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        logger.info("We should get till this point ! ");
                        user.addUserReward(new UserReward(visitedLocation, attraction, rewardCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId())));
                        logger.info("///////// This user " + user.getUserName() + "has " + user.getUserRewards().size());

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

}
