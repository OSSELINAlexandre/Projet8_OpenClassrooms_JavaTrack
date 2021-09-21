package GpsUtilApp.service;

import GpsUtilApp.model.*;
import GpsUtilApp.proxy.RewardCentralProxy;
import gpsUtil.GpsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GpsUtilService {

    @Autowired
    RewardCentralProxy rewardCentralProxy;

    private static Logger logger = LoggerFactory.getLogger(GpsUtilService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private int defaultProximityBuffer = Integer.MAX_VALUE;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = Integer.MAX_VALUE;

    private final GpsUtil gpsUtil;


    public GpsUtilService(){

        gpsUtil = new GpsUtil();
    }


    public VisitedLocation getTheUser(UUID theId) {

        gpsUtil.location.VisitedLocation intermediaryResult = gpsUtil.getUserLocation(theId);
        gpsUtil.location.Location intermediaryLoc = intermediaryResult.location;
        Location resultLoc = new Location(intermediaryLoc.latitude, intermediaryLoc.longitude);
        VisitedLocation result = new VisitedLocation(intermediaryResult.userId, resultLoc, intermediaryResult.timeVisited);


        return result;

    }

    public List<Attraction> getAllAttraction() {

        List<gpsUtil.location.Attraction> mediumResult = gpsUtil.getAttractions();
        List<Attraction> result = new ArrayList<>();
        mediumResult.forEach(n -> result.add(new Attraction(n.attractionName, n.city, n.state, n.latitude, n.longitude)));

        return result;
    }

    public User trackTheUser(User user) {

        VisitedLocation visitedLocation = getTheUser(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        calculateRewards(user);

        return user;


    }

    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = getAllAttraction();

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
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                        logger.info("///////// This user " + user.getUserName() + "has " + user.getUserRewards().size());
                    }
                }
            }
        }

    }

    public List<UserNearbyAttraction> getNearByFifthClosestAttractions(User u) {

        VisitedLocation visitedLocation = (u.getVisitedLocations().size() > 0 ? u.TheLastVisitedLocation() : trackTheUser(u).TheLastVisitedLocation());
        logger.info("DO WE GET HERE MAN ????? " + visitedLocation + " it's a hard job for freemon " + u.TheLastVisitedLocation());
        List<UserNearbyAttraction> nearbyAttractions = new ArrayList<>();
        List<Attraction> resultAttraction = new ArrayList<>();

        Double distancedToBeChanged = 0.0;
        Integer indexToBeChange = null;

        for (Attraction attraction : getAllAttraction()) {

            if (resultAttraction.size() < 5) {

                resultAttraction.add(attraction);

            } else {

                for (int i = 0; i < resultAttraction.size(); i++) {

                    Location attractionLoc = new Location(attraction.latitude, attraction.longitude);

                    Double attractionTestDist = getDistance(attractionLoc, visitedLocation.location);
                    Double attractionBeingTested = getDistance(resultAttraction.get(i),
                            visitedLocation.location);

                    if (attractionTestDist < attractionBeingTested) {

                        if (attractionBeingTested > distancedToBeChanged) {

                            indexToBeChange = i;
                            distancedToBeChanged = attractionBeingTested;

                        }

                    }
                }

                if (indexToBeChange != null) {

                    resultAttraction.set(indexToBeChange, attraction);
                    indexToBeChange = null;
                    distancedToBeChanged = 0.0;

                }

            }

        }

        for (Attraction e : resultAttraction) {

            UserNearbyAttraction newItem = new UserNearbyAttraction();
            newItem.setUserLocation(visitedLocation.location);
            newItem.setTouristAttractionName(e.attractionName);
            Location attractionLoc = new Location(e.latitude, e.longitude);
            newItem.setAttractionLocation(attractionLoc);
            newItem.setDistanceInMilesBetweenUserAndAttraction(getDistance(e, visitedLocation.location));

            /**
             * Think to see about the attractionRewardsPoint and send the user, not some
             * null thing.
             */
            newItem.setRewardsLinkedToTheAttraction(
                    getRewardPoints(e, u));
            nearbyAttractions.add(newItem);
        }

        return nearbyAttractions;
    }


    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    public int getRewardPoints(Attraction attraction, User user) {
        logger.info("I think it's called when we do an Add but not automatically");
        return rewardCentralProxy.getTheReward(attraction.attractionId, user.getUserId());
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

    public List<Location> getAllTheList(List<User> users) {

        CopyOnWriteArrayList<Location> result = new CopyOnWriteArrayList<>();
        for(User u : users){

            Location visitedLocation = (u.getVisitedLocations().size() > 0) ? u.TheLastVisitedLocation().location
                    : trackTheUser(u).TheLastVisitedLocation().location;

            result.add(visitedLocation);
        }

        return result;
    }

    public void setTheReward(RewardCentralProxy aRewardWay){
        this.rewardCentralProxy = aRewardWay;
    }
}
