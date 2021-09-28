package UserApp.service;

import UserApp.dto.UserAndAttractionDTO;
import UserApp.dto.UserGpsDTO;
import UserApp.dto.UserPreferencesDTO;
import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.testers.InternalUsersSetters;
import UserApp.tracker.Tracker;
import org.javamoney.moneta.Money;
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

    @Autowired
    RewardProxy rewardProxy;



    public Tracker tracker;
    public static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    public final static ReentrantLock lock = new ReentrantLock();
    public static Boolean testMode = false;


    // ***********************************************************************************************************
    // ************                            INITIALIZING & TEST part                   ************************
    // ***********************************************************************************************************

    public UserService(){

        if(testMode){


        }else{

            initializeAListOfUser();
            tracker = new Tracker(this);

        }


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



    // ***********************************************************************************************************
    // ************                            User Domain Services                       ************************
    // ***********************************************************************************************************

    public User addAUser(User user) {

        List<UUID> verification = new ArrayList<>();
        users.forEach(n -> verification.add(n.getUserId()));

        if(!verification.contains(user.getUserId())) {

            users.add(user);
            int indexOfUser = users.indexOf(user);
            User theUser = users.get(indexOfUser);
            return theUser;

        }else{

            return null;

        }

    }

    public Boolean deleteAUser(String userName) {


        User result = getTheUserBasedOnName(userName);

        if(result != null){
            users.remove(result);
            return true;
        }
        return false;
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

    public UserPreferences getUserPreference(String userName) {

        User result = getTheUserBasedOnName(userName);
        UserPreferences userResult = result.getUserPreferences();
        return userResult;
    }


    public CopyOnWriteArrayList<UserReward> getUserRewards(String userName) {

        User result = getTheUserBasedOnName(userName);
        return result.getUserRewards();

    }

    public User getASpecificUser(String user) {
        User theUser = getTheUserBasedOnName(user);
        return theUser;
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


    // ***********************************************************************************************************
    // ************                            LIEE AU Domain du GPS                      ************************
    // ***********************************************************************************************************

    //@TODO I think that this method shoud just send some Location information, no saving. Just the tracker should do so.
    public VisitedLocation getUserLocation(String userName) {

        User theUser = getTheUserBasedOnName(userName);

        if(theUser != null){
            UserGpsDTO resultToSend = transformUserIntoUserGpsDto(theUser);
            UserGpsDTO updatedResult = gpsUtilProxy.getTheLocation(resultToSend);
            return updatedResult.getVisitedLocations().get(updatedResult.getVisitedLocations().size() - 1);

        }else{

            return null;
        }

    }

    public List<Attraction> getAllAttraction() {

        List<Attraction> result = gpsUtilProxy.getAllAttraction();
        return result;
    }

    //TODO do not send a random UUID
    public List<UserNearbyAttraction> getAllFifthClosestAttraction(String userName) {

        User theUser = getTheUserBasedOnName(userName);

        if(theUser != null){

            UserGpsDTO resultToBeSend = transformUserIntoUserGpsDto(theUser);

            List<UserNearbyAttraction> result = gpsUtilProxy.getFifthClosestAttraction(resultToBeSend);


            //TODO remettre une boucle dans le getRewards.
            result.forEach(n -> n.setRewardsLinkedToTheAttraction(rewardProxy.getTheReward(UUID.randomUUID(), resultToBeSend.getUserId())));

            return result;

        }

        return null;
    }


    public List<VisitedLocation> getAllLocationOfUsers() {

        CopyOnWriteArrayList<UserGpsDTO> toBeSendResult = new CopyOnWriteArrayList<>();
        users.forEach( n -> toBeSendResult.add( transformUserIntoUserGpsDto( n )));
        List<UserGpsDTO> result = gpsUtilProxy.getAllLocationOfUsers(toBeSendResult);
        List<User> intermediaryResult = new ArrayList<>();
        for(UserGpsDTO gpsDto : result){
            intermediaryResult.add(saveTheUserDTOAndReturnUser(gpsDto));
        }
        List<VisitedLocation> finalRenderingList = new ArrayList<>();

        for(User u : intermediaryResult){
            finalRenderingList.add(u.getLastVisitedLocation());
        }


        return finalRenderingList;
    }



    // ***********************************************************************************************************
    // ************                            LIEE AU TripPricerProxy                    ************************
    // ***********************************************************************************************************


    //TODO check a quoi sert la clé API et où la mettre !
    public List<Provider> getAllTheDeals(String userName) {

        User theUser = getTheUserBasedOnName(userName);
        int totalAmountOfRewards = 0;
        for(UserReward s : theUser.getUserRewards()){
            totalAmountOfRewards += s.getRewardPoints();
        }
        return tripPricerProxy.getPrices("Hey", theUser.getUserId(), theUser.getUserPreferences().getNumberOfAdults(), theUser.getUserPreferences().getNumberOfChildren(), theUser.getUserPreferences().getTripDuration(), totalAmountOfRewards);
    }


    // ***********************************************************************************************************
    // ************                            LIEE AU Reward Domain                      ************************
    // ***********************************************************************************************************

    public int getAttractionRewardsPoints(UUID attraction, UUID user) {

        return rewardProxy.getTheReward(attraction, user);

    }

    public User calculateTheRewardsOfUser(User u, List<Attraction> attractions){

        UserAndAttractionDTO sendingResult = transformUserIntoUserAndAttractionDTO(u, attractions);
        UserAndAttractionDTO updatedResultTwo = rewardProxy.calculateTheUserReward(sendingResult);
        User result = saveTheUserAndAttractionDTOreturnUser(updatedResultTwo);
        return result;
    }


    public Map<String, List<UserReward>> getAllRewardsPointsOfUsers(List<Attraction> attractionList) {

        List<UserAndAttractionDTO> toBeSentList = new ArrayList<>();
        Map<String, List<UserReward>> finalResult = new HashMap<>();

        for(User u : users){

            toBeSentList.add(transformUserIntoUserAndAttractionDTO(u, attractionList));
        }

        List<UserAndAttractionDTO> result = rewardProxy.calculateTheRewardsForAllTheUsers(toBeSentList);

        for(UserAndAttractionDTO u : result){

            saveTheUserAndAttractionDTOreturnUser(u);
        }
        for(User user : users){

            finalResult.put(user.getUserName(), user.getUserRewards());

        }

        return finalResult;

    }







    // ***********************************************************************************************************
    // ************                            THE TRACKER                                ************************
    // ***********************************************************************************************************


    public void trackUserLocation() {


        CopyOnWriteArrayList<UserGpsDTO> toBeSendData = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<UserAndAttractionDTO> toBeSendDataAttraction = new CopyOnWriteArrayList<>();
        List<Attraction> allAttractions = gpsUtilProxy.getAllAttraction();

        for(User u : users){
            toBeSendData.add(transformUserIntoUserGpsDto(u));
        }
        List<UserGpsDTO> newResult = gpsUtilProxy.getAllLocationOfUsers(toBeSendData);

        for(UserGpsDTO gpsDto : newResult){
            saveTheUserDTOAndReturnUser(gpsDto);
        }
        for(User u : users){
            toBeSendDataAttraction.add(transformUserIntoUserAndAttractionDTO(u, allAttractions));
        }
        List<UserAndAttractionDTO> updatedResultTwo = rewardProxy.calculateTheRewardsForAllTheUsers(toBeSendDataAttraction);
        for(UserAndAttractionDTO attDto : updatedResultTwo){

            saveTheUserAndAttractionDTOreturnUser(attDto);
        }
    }




    // ***********************************************************************************************************
    // ************                            UTIL                                        ************************
    // ***********************************************************************************************************


    public User getTheUserBasedOnName(String userName) {

       Optional<User> result = users.stream().filter(n -> n.getUserName().equals(userName)).findAny();


        return result.orElse(null);
    }

    public User saveTheUserDTOAndReturnUser(UserGpsDTO newResult) {

        User result = getTheUserBasedOnId(newResult.getUserId());
        int indexOfResult = users.indexOf(result);
        result.setVisitedLocations(newResult.getVisitedLocations());
        users.remove(indexOfResult);
        users.add(indexOfResult, result);
        return result;
    }


    public User getTheUserBasedOnId(UUID theId){

        Optional<User> result = users.stream().filter(n -> n.getUserId().equals(theId)).findAny();

        return result.orElse(null);

    }


    public UserGpsDTO transformUserIntoUserGpsDto(User theUser) {

        UserGpsDTO result = new UserGpsDTO();
        result.setEmailAddress(theUser.getEmailAddress());
        result.setLatestLocationTimestamp(theUser.getLatestLocationTimestamp());
        result.setPhoneNumber(theUser.getPhoneNumber());
        result.setUserId(theUser.getUserId());
        result.setUserName(theUser.getUserName());
        result.setVisitedLocations(theUser.getVisitedLocations());

        return result;
    }

    public User saveTheUserAndAttractionDTOreturnUser(UserAndAttractionDTO updatedResultTwo) {


        // L'erreur est là, enfaite, comme une nouvelle ArrayList est créee dans le DTO
        //alors on rajoute à chaque fois la liste des résultats lorsque la requête revient, ce qui superpose
        // des résultats pourtants indentiques.
        User result = getTheUserBasedOnId(updatedResultTwo.getUserId());
        int indexOfResult = users.indexOf(result);
        result.setUserRewards(updatedResultTwo.getUserRewards());
        users.remove(indexOfResult);
        users.add(indexOfResult, result);
        return result;
    }



    //TODO je suis obligé de refaire le lien avec le Users pour que les bonnes données soient présentes.
    public UserAndAttractionDTO transformUserIntoUserAndAttractionDTO(User newResult, List<Attraction> attractions) {

        UserAndAttractionDTO result = new UserAndAttractionDTO();
        //result.getVisitedLocations().add((newResult.getVisitedLocations().get(newResult.getVisitedLocations().size() -1)));
        result.getVisitedLocations().add((newResult.getLastVisitedLocation()));
        result.setUserRewards(newResult.getUserRewards());
        result.setUserName(newResult.getUserName());
        result.setUserId(newResult.getUserId());
        result.setPhoneNumber(newResult.getPhoneNumber());
        result.setLatestLocationTimestamp(newResult.getLatestLocationTimestamp());
        result.setEmailAddress(newResult.getEmailAddress());
        result.setAttractions(attractions);

        return result;

    }


    // ***********************************************************************************************************
    // ************                Getters and Setters for testing purposes               ************************
    // ***********************************************************************************************************

    public GpsUtilProxy getGpsUtilProxy() {
        return gpsUtilProxy;
    }

    public void setGpsUtilProxy(GpsUtilProxy gpsUtilProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
    }

    public TripPricerProxy getTripPricerProxy() {
        return tripPricerProxy;
    }

    public void setTripPricerProxy(TripPricerProxy tripPricerProxy) {
        this.tripPricerProxy = tripPricerProxy;
    }

    public RewardProxy getRewardProxy() {
        return rewardProxy;
    }

    public void setRewardProxy(RewardProxy rewardProxy) {
        this.rewardProxy = rewardProxy;
    }

    public void setTheProfileTrueForTestFalseForExperience(Boolean result){

        this.testMode = result;
    }


}
