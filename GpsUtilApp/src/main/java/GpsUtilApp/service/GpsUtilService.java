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

/**
 * <p>GpsUtilService is the service in charge of the business logic, making the link between the client (via the controller) and the library by providing added value.</p>
 * <p>The library is present for building the whole structure of the microservices of this project : once the application is validated, we can simply communicate with an online API instead of a library.</p>
 * <p>There is some noticeable attributes :</p>
 * <ul>
 *     <li>The executorService is our thread pool.</li>
 *     <li>GpsUtil is the library provided by the client.</li>
 * </ul>
 *
 */
@Service
public class GpsUtilService {


    private static Logger logger = LoggerFactory.getLogger(GpsUtilService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    public  ExecutorService executorService;
    private final GpsUtil gpsUtil;


    public GpsUtilService(){

        gpsUtil = new GpsUtil();
    }


    /**
     *
     * <p>getTheUser generate a new visited location for the user entered as a parameter.</p>
     * <p>getTheUser is also a utility method that makes the link between the type of the library and the type of the whole system (VisitedLocation and Location).</p>
     *
     * @param theId
     * @return
     */
    public VisitedLocation getTheUser(UUID theId) {

        gpsUtil.location.VisitedLocation intermediaryResult = gpsUtil.getUserLocation(theId);
        gpsUtil.location.Location intermediaryLoc = intermediaryResult.location;
        Location resultLoc = new Location(intermediaryLoc.latitude, intermediaryLoc.longitude);
        VisitedLocation result = new VisitedLocation(intermediaryResult.userId, resultLoc, intermediaryResult.timeVisited);


        return result;

    }

    /**
     * <p>getAllAttraction recuperate form the GpsUtilLibrary the list of all the attraction available. Therefore, it does not need any parameters.</p>
     * <p>It is also a utility method because it transform from the type of the library to a standard type understood by all the microservices of the system.</p>
     * @return List<Attraction>
     */
    public List<Attraction> getAllAttraction() {

        List<gpsUtil.location.Attraction> mediumResult = gpsUtil.getAttractions();
        List<Attraction> result = new ArrayList<>();
        mediumResult.forEach(n -> result.add(new Attraction(n.attractionName, n.city, n.state, n.latitude, n.longitude)));

        return result;
    }

    /**
     *
     * <p>TrackTheUser is the method that add a new 'VisitedLocation' to the user.</p>
     *
     * @param user
     *          The user from whom we want to add a new 'VisitedLocation'.
     *
     * @return User
     */
    public User trackTheUser(User user) {

        VisitedLocation visitedLocation = getTheUser(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        return user;


    }


    /**
     * <p>getNearByFiveClosestAttractions is the method that return the five closest attractions from a user.</p>
     * <p>If the user has not saved visited location, the method will track him in order to know its current location.</p>
     * <p>If the user already has at least one visited location, the method will return the closest five attraction from the last visited location available in the attributes of the user.</p>
     *
     *
     * @param user
     * @return
     */
    public List<UserNearbyAttraction> getNearByFiveClosestAttractions(User user) {

        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0 ? user.TheLastVisitedLocation() : trackTheUser(user).TheLastVisitedLocation());
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
            newItem.setTheAttraction(e);
            newItem.setDistanceInMilesBetweenUserAndAttraction(getDistance(e, visitedLocation.location));

            nearbyAttractions.add(newItem);
        }

        return nearbyAttractions;
    }


    /**
     *
     * <p>getDistance if the mathematical formula needed to calculate a distance on a plan.</p>
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
     * <p>getAllTheList is the principal method used by the User Application.</p>
     * <p>With a list of users as a parameters, it will send back an updated list with new visited location.</p>
     * <p>Because of performance requirements by our client, and because of preinstalled latencies in the library, we created a fixeThreadPool.</p>
     *
     * @param users
     *          The list of users from which we want to have new visited locations.
     * @return List<User>
     */
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
