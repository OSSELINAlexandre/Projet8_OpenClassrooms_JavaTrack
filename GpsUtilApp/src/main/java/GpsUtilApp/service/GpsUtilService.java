package GpsUtilApp.service;

import GpsUtilApp.model.*;
import gpsUtil.GpsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class GpsUtilService {


    private static Logger logger = LoggerFactory.getLogger(GpsUtilService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private int defaultProximityBuffer = Integer.MAX_VALUE;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = Integer.MAX_VALUE;
    public  ExecutorService executorService;

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
        return user;


    }


    public List<UserNearbyAttraction> getNearByFifthClosestAttractions(User u) {

        VisitedLocation visitedLocation = (u.getVisitedLocations().size() > 0 ? u.TheLastVisitedLocation() : trackTheUser(u).TheLastVisitedLocation());
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

            nearbyAttractions.add(newItem);
        }

        return nearbyAttractions;
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

    public List<User> getAllTheList(List<User> users) {
        executorService = Executors.newFixedThreadPool(1000);

        CopyOnWriteArrayList<User> result = new CopyOnWriteArrayList<>();

        for(User u : users){

            logger.info("---The user : " + u.getUserName() +" / TrackTheUser to getAllList");

            CompletableFuture.supplyAsync(() -> result.add(trackTheUser(u)), executorService);

        }



        try {
            executorService.shutdown();
            executorService.awaitTermination(15, TimeUnit.MINUTES);
        }catch(Exception e){
            executorService.shutdown();
        }
        logger.info("===List size sent from GpsUtilApp : " + result.size());


        return result;
    }

}
