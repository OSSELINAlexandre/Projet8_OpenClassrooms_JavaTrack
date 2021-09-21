package UserApp.service;

import UserApp.dto.UserDTO;
import UserApp.dto.UserPreferencesDTO;
import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.testers.InternalUsersSetters;
import UserApp.tracker.Tracker;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class UserService {

    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    private Logger logger = LoggerFactory.getLogger(UserService.class);


    public Tracker tracker;
    public static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    public final static ReentrantLock lock = new ReentrantLock();
    public static Boolean testMode = true;

    /*
    *
    * This part is present in order to create some Data for testing our application.
    *
    *
    *
    *
    * */

    public UserService(){

        if(testMode){

            initializeAListOfUser();
        }
        tracker = new Tracker(this);
    }

    private void initializeAListOfUser() {

        IntStream.range(0, InternalUsersSetters.numberOfWantedUsers).forEach( i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            users.add(user);
        });



    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
                    new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }



    /*
    *
    * Part in relationship with the controllers.
    *
    *
    * */


    private UserDTO tranformUserIntoDto(User user) {

        UserDTO result = new UserDTO();
        UserPreferencesDTO resultPref = new UserPreferencesDTO();

        // Setting all characteristics from the UserDTO
        result.setLatestLocationTimestamp(user.getLatestLocationTimestamp());
        result.setEmailAddress(user.getEmailAddress());
        result.setPhoneNumber(user.getPhoneNumber());
        result.setUserId(user.getUserId());
        result.setUserRewards(user.getUserRewards());
        result.setUserName(user.getUserName());
        result.setTripDeals(user.getTripDeals());
        result.setVisitedLocations(user.getVisitedLocations());


        UserPreferences formerPref = user.getUserPreferences();
        resultPref.setAttractionProximity(formerPref.getAttractionProximity());
        resultPref.setCurrency(formerPref.getCurrency().getCurrencyCode());
        resultPref.setHighPricePoint(formerPref.getHighPricePoint().getNumber().intValue());
        resultPref.setLowerPricePoint(formerPref.getLowerPricePoint().getNumber().intValue());
        resultPref.setNumberOfAdults(formerPref.getNumberOfAdults());
        resultPref.setTicketQuantity(formerPref.getTicketQuantity());
        resultPref.setNumberOfChildren(formerPref.getNumberOfChildren());
        resultPref.setTripDuration(formerPref.getTripDuration());
        result.setUserPreferences(resultPref);

        return result;

    }

    private User tranformDTOintoUser(UserDTO user) {

        User result = new User();
        UserPreferences resultPref = new UserPreferences();

        // Setting all characteristics from the UserDTO
        result.setLatestLocationTimestamp(user.getLatestLocationTimestamp());
        result.setEmailAddress(user.getEmailAddress());
        result.setPhoneNumber(user.getPhoneNumber());
        result.setUserId(user.getUserId());
        result.setUserRewards(user.getUserRewards());
        result.setUserName(user.getUserName());
        result.setTripDeals(user.getTripDeals());
        result.setVisitedLocations(user.getVisitedLocations());


        UserPreferencesDTO formerPref = user.getUserPreferences();

        resultPref.setAttractionProximity(formerPref.getAttractionProximity());
        resultPref.setCurrency(Monetary.getCurrency(formerPref.getCurrency()));
        resultPref.setLowerPricePoint(Money.of(formerPref.getLowerPricePoint(), resultPref.getCurrency()));
        resultPref.setHighPricePoint(Money.of(formerPref.getHighPricePoint(), resultPref.getCurrency()));
        resultPref.setNumberOfAdults(formerPref.getNumberOfAdults());
        resultPref.setTicketQuantity(formerPref.getTicketQuantity());
        resultPref.setNumberOfChildren(formerPref.getNumberOfChildren());
        resultPref.setTripDuration(formerPref.getTripDuration());
        result.setUserPreferences(resultPref);

        return result;

    }

    public User addAUser(User user) {

        UserDTO transform = tranformUserIntoDto(user);
        UserDTO transformWithResult = gpsUtilProxy.getTheLocation(transform);
        User result = tranformDTOintoUser(transformWithResult);
        users.add(result);

        int theIndex = users.indexOf(result);
        User theUser = users.get(theIndex);

        return theUser;
    }


    public VisitedLocation trackUserLocation(User u) {

        int indexOfUser = users.indexOf(u);
        UserDTO resultToSend = tranformUserIntoDto(u);
        UserDTO newResult = gpsUtilProxy.getTheLocation(resultToSend);
        User finalResult = tranformDTOintoUser(newResult);

        users.remove(indexOfUser);
        users.add(indexOfUser, finalResult);

        return finalResult.getLastVisitedLocation();

    }

    public Boolean deleteAUser(String userName) {


        User result = getTheUserBasedOnName(userName);

        if(result != null){
            users.remove(result);
            return true;
        }
        return false;
    }

    public VisitedLocation getUserLocation(String userName) {

        User theUser = getTheUserBasedOnName(userName);
        int indexOfUser = users.indexOf(theUser);
        UserDTO toBeSendResult = tranformUserIntoDto(theUser);
        UserDTO updatedResult = gpsUtilProxy.getTheLocation(toBeSendResult);
        User result = tranformDTOintoUser(updatedResult);

        users.remove(indexOfUser);
        users.add(indexOfUser, result);

        return result.getLastVisitedLocation();
    }

    public List<Attraction> getAllAttraction() {

        List<Attraction> result = gpsUtilProxy.getAllAttraction();
        return result;
    }

    public User updateUserPreferences(String user, UserPreferencesDTO userPref) {

        User userFound = getTheUserBasedOnName(user);
        if(userFound != null) {

            int indexOfUser = users.indexOf(userFound);

            User newUser = userFound;
            UserPreferences lol = new UserPreferences();
            lol.setCurrency(Monetary.getCurrency(userPref.getCurrency()));
            lol.setLowerPricePoint(Money.of(userPref.getLowerPricePoint(), lol.getCurrency()));
            lol.setHighPricePoint(Money.of(userPref.getHighPricePoint(), lol.getCurrency()));
            lol.setNumberOfAdults(userPref.getNumberOfAdults());
            lol.setTripDuration(userPref.getTripDuration());
            lol.setTicketQuantity(userPref.getTicketQuantity());
            lol.setNumberOfChildren(userPref.getNumberOfChildren());
            lol.setAttractionProximity(userPref.getAttractionProximity());
            newUser.setUserPreferences(lol);
            users.remove(indexOfUser);
            users.add(indexOfUser, newUser);
            return userFound;
        }

        return null;
    }

    private User getTheUserBasedOnName(String userName) {

       Optional<User> result = users.stream().filter(n -> n.getUserName().equals(userName)).findAny();


        return result.orElse(null);
    }

    public CopyOnWriteArrayList<UserReward> getUserRewards(String userName) {

        User result = getTheUserBasedOnName(userName);
        return result.getUserRewards();

    }

    public UserPreferences getUserPreference(String userName) {

        User result = getTheUserBasedOnName(userName);
        UserPreferences userResult = result.getUserPreferences();
        return userResult;
    }

    public CopyOnWriteArrayList<VisitedLocation> getAllUserLocationGivenUser(String userName) {

        User result = getTheUserBasedOnName(userName);

        if(result != null){

            return result.getVisitedLocations();
        }

        return null;
    }

    public List<VisitedLocation> getAllLastLocationUsers() {
        CopyOnWriteArrayList<VisitedLocation> result = new CopyOnWriteArrayList<VisitedLocation>();
        users.forEach(n -> result.add(n.getLastVisitedLocation()));
        return result;
    }

    public List<UserNearbyAttraction> getAllFifthClosestAttraction(String userName) {

        Optional<User> theUser = users.stream().filter(n -> n.getUserName().equals(userName)).findAny();
        if(theUser.isPresent()){


         UserDTO sendingResult = tranformUserIntoDto(theUser.get());

        return gpsUtilProxy.getFifthClosestAttraction(sendingResult);

        }

        return null;
    }

    //TODO check a quoi sert la clé API et où la mettre !
    public List<Provider> getAllTheDeals(String userName) {

        User theUser = getTheUserBasedOnName(userName);
        int totalAmountOfRewards = 0;
        for(UserReward s : theUser.getUserRewards()){
            totalAmountOfRewards += s.getRewardPoints();
        }
        return tripPricerProxy.getPrices("Hey", theUser.getUserId(), theUser.getUserPreferences().getNumberOfAdults(), theUser.getUserPreferences().getNumberOfChildren(), theUser.getUserPreferences().getTripDuration(), totalAmountOfRewards);
    }

    public User getASpecificUser(String user) {
        User theUser = getTheUserBasedOnName(user);
        return theUser;
    }
}
