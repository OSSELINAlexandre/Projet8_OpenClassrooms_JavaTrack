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

/**
 *
 * <p>UserService is the service class that centralizes all the logic of the Users Application. </p>
 *
 * <p> UserService makes the link between all the proxies (which are the method signatures of all endpoints of the microservices).</p>
 * <p>It also makes the link between theses proxies and the tracker (which is the simulation of functioning of the real life application).</p>
 *
 *
 */
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
    // ************ This part of the code is used in order to simulate the usage by users ************************
    // ***********************************************************************************************************

    /**
     * <p>The constructor of the UserService</p>
     * <p>If testMode is set to true, it creates a list of Users and launch the tracker for these users.</p>
     */
    public UserService(){
            if(!testMode) {
                initializeAListOfUser();
                tracker = new Tracker(this);
            }
    }


    /**
     * <p>initializeAListOfUser initialize a list of 'users' of type User that would be registered to the app for testing purposes.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     */
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

    /**
     * generateUserLocationHistory is called by initializeUser in order to create mocks of visited Location of the user.
     *
     * @param user
     *          A user from whom we want to generate a location history for testing purposes.
     */
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
                    new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    /**
     * <p>generateRandomLongitude generate a random longitude for testing purposes.</p>
     *
     * @return double
     */
    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * <p>generateRandomLatitude generate a random latitude for testing purposes.</p>
     *
     * @return double
     */
    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * <p>getRandomTime generate a random time for testing purposes.</p>
     *
     * @return Date
     */
    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }



    // ***********************************************************************************************************
    // ******** This part of the code correspond to the User Domain (managing the users themselves). *************
    // ***********************************************************************************************************

    /**
     * <p> addAUser takes a User as an argument and add it to the list of the application's users.</p>
     * <p> The method returns null if the users already exist in the list of the application's users.</p>
     * <p> The list of all current registered users is called 'users' in the userService.</p>
     * @param user
     *            The user that was posted by the client of our application.
     * @return User
     */
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

    /**
     *
     * <p>deleteAUser delete a user from the list of the user's of the application. </p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     * <p>It returns true if the user exists, and false otherwise.</p>
     *
     * @param userName
     *                The userName posted by the client of our application.
     * @return Boolean
     */
    public Boolean deleteAUser(String userName) {


        User result = getTheUserBasedOnName(userName);

        if(result != null){
            users.remove(result);
            return true;
        }
        return false;
    }

    /**
     *
     * <p>updateUserPreferences takes a string and UserPreferences as arguments. </p>
     * <p>The method first check if the users exist in the list of the current users of the application</p>
     * <p>It then changes the preferences of the given users to news preferences (that's where given as argument).</p>
     * <p>Because of some incompatibility with JavaMoney library and Jackson (and more generally with Java to Json libraries) we use a DTO to register the new preferences.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     * @see UserPreferencesDTO
     * @see UserPreferences
     * @param user
     *            The user's 'post' by the client of our application.
     * @param userPref
     *            The new preferences wanted by the client of our application to change the current one's.
     * @return User
     */
    public User updateUserPreferences(String user, UserPreferencesDTO userPref) {

        User userFound = getTheUserBasedOnName(user);
        if(userFound != null) {

            int indexOfUser = users.indexOf(userFound);

            User newUser = userFound;
            UserPreferences newPreferences = new UserPreferences();
            newPreferences.setCurrency(Monetary.getCurrency(userPref.getCurrency()));
            newPreferences.setLowerPricePoint(Money.of(userPref.getLowerPricePoint(), newPreferences.getCurrency()));
            newPreferences.setHighPricePoint(Money.of(userPref.getHighPricePoint(), newPreferences.getCurrency()));
            newPreferences.setNumberOfAdults(userPref.getNumberOfAdults());
            newPreferences.setTripDuration(userPref.getTripDuration());
            newPreferences.setTicketQuantity(userPref.getTicketQuantity());
            newPreferences.setNumberOfChildren(userPref.getNumberOfChildren());
            newPreferences.setAttractionProximity(userPref.getAttractionProximity());
            newUser.setUserPreferences(newPreferences);
            users.remove(indexOfUser);
            users.add(indexOfUser, newUser);
            return userFound;
        }

        return null;
    }

    /**
     *
     * <p>getUserPreference request the name of a user as a parameter and return the preferences of this given user.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     * @see UserPreferences
     * @param userName
     *              The userName posted by the client of our application.
     * @return
     */
    public UserPreferences getUserPreference(String userName) {

        User result = getTheUserBasedOnName(userName);
        UserPreferences userResult = result.getUserPreferences();
        return userResult;
    }


    /**
     *
     * <p> getUserRewards takes a userName as an argument and returns the list of all rewards for this given user.</p>
     *
     * <p>This method will return and empty list if the User hasn't received any rewards.</p>
     * <p>The rewards policy is fixed by the Microservice (RewardsApp in our case).</p>
     * <p>Therefore, depending on the reward policy set, a user can receive all possible rewards for every attraction or really few.</p>
     * <p>The return results of this method will depend on this.</p>
     * <p>It returns null if the user does not exist in the current users of the application.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     * @param userName
     *              The userName posted by the client of our application.
     * @return CopyOnWriteArrayList<UserReward>
     */
    public CopyOnWriteArrayList<UserReward> getUserRewards(String userName) {

        User result = getTheUserBasedOnName(userName);
        return result.getUserRewards();

    }

    /**
     * <p>getASpecificUser take a string as an argument, check if the user exists in the current list of all users of the application</p>
     * <p>The method return the user.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     *
     * @see User
     * @param user
     *          The userName that we intend to find in the list of users.
     * @return User
     */
    public User getASpecificUser(String user) {
        User theUser = getTheUserBasedOnName(user);
        return theUser;
    }

    /**
     *
     * <p>getAllUserLocationGivenUser take a string as an argument, check if the provided name relates to a user of the application and
     * returns the list of all visited locations for this given user.
     * </p>
     * <p>It returns null if the user isn't registered as a user of the application.</p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     * @see VisitedLocation
     * @param userName
     *              The userName posted by the client of our application.
     * @return CopyOnWriteArrayList<VisitedLocation>
     */
    public CopyOnWriteArrayList<VisitedLocation> getAllUserLocationGivenUser(String userName) {

        User result = getTheUserBasedOnName(userName);

        if(result != null){

            return result.getVisitedLocations();
        }

        return null;
    }

    /**
     *
     * <p>getAllLastLocationUsers returns all the visited locations of all current users of the application. </p>
     * <p>The list of all current registered users is called 'users' in the userService.</p>
     *
     * @see VisitedLocation
     * @return List<VisitedLocation>
     */
    public List<VisitedLocation> getAllLastLocationUsers() {
        CopyOnWriteArrayList<VisitedLocation> result = new CopyOnWriteArrayList<VisitedLocation>();
        users.forEach(n -> result.add(n.getLastVisitedLocation()));
        return result;
    }


    // *****************************************************************************************************************************************
    // This part correspond off all logic corresponding to the Gps Domain (and therefore the link with the corresponding microservice GpsUtilApp)
    // *****************************************************************************************************************************************

    /**
     *
     * <p>getUserLocation takes a userName as a parameter. It firsts checks if the given name corresponds to a user in the Users list.</p>
     * <p>it then send solely the required information's (explaining the transformation into a DTO) via the GpsUtil proxy.</p>
     * <p>It finally returns the last visited location of the user (that has been added by the API).</p>
     * <p>If the user does not exist in the application current users, it returns null. </p>
     *
     * @param userName
     *              The user's name from who we want to know the location.
     * @return VisitedLocation
     */
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

    /**
     *
     * <p>getAllAttraction returns all the attractions provided by the provided microservice via the proxy.</p>
     *
     *
     * @return List<Attraction>
     */
    public List<Attraction> getAllAttraction() {

        List<Attraction> result = gpsUtilProxy.getAllAttraction();
        return result;
    }


    /**
     *
     * <p>getAllFifthClosestAttraction is a requirement from the client.</p>
     * <p>getAllFifthClosestAttraction takes a userName as a parameter.</p>
     * <p>The method first check if the given name corresponds to a current user of the application.</p>
     * <p>It then send solely the required information's (explaining the transformation into a DTO) via the GpsUtil proxy.</p>
     * <p>It returns a list of five UserNearbyAttraction, that are the closest attractions from the last saved 'VisitedLocation'.</p>
     * <p>The method then calculates the rewards points related to the attractions via the RewardProxy.</p>
     *
     *
     * @see RewardProxy
     * @see GpsUtilProxy
     * @param userName
     *              The user's name from whom we want to find the five closest attraction.
     * @return
     */
    public List<UserNearbyAttraction> getAllFiveClosestAttraction(String userName) {

        User theUser = getTheUserBasedOnName(userName);
        if(theUser != null){

            UserGpsDTO resultToBeSend = transformUserIntoUserGpsDto(theUser);

            List<UserNearbyAttraction> result = gpsUtilProxy.getFiveClosestAttraction(resultToBeSend);
            result.forEach(n -> n.setRewardsLinkedToTheAttraction(rewardProxy.getTheReward(n.getTheAttraction().attractionId, resultToBeSend.getUserId())));

            return result;

        }

        return null;
    }


    /**
     * <p>getAllLocationOfUsers returns all the last visited locations of all the users' registered in the application.</p>
     * <p>It first transform the list of all current users into the corresponding DTO in order to send solely the required information's. </p>
     * <p>It then sends the list to the API in charge of the localization of the users (in our case, GpsUtilApp) via the Proxy.</p>
     * <p>Once the result is returned, it transforms back the DTO to the corresponding User type and returns a list of the last 'VisitedLocation'</p>
     *
     *
     * @return List<VisitedLocation>
     */
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



    // **********************************************************************************************************************************************
    // This part correspond off all logic corresponding to the TripApp Domain (and therefore the link with the corresponding microservice TripApp).
    // **********************************************************************************************************************************************


    //TODO check a quoi sert la clé API et où la mettre !
    /**
     * <p>getAllTheDeals take a userName as parameter and check if the name correspond to a current user of the application</p>
     * <p>It then send all the preferences of the given user to the API in charge of the pricing of trips (in our case, TripPricerApp).</p>
     * <p>It returns the list of all providers.</p>
     *
     * @see Provider
     * @param userName
     *              The user's name from who we want to know the get all the deals.
     * @return List<Provider>
     */
    public List<Provider> getAllTheDeals(String userName) {

        User theUser = getTheUserBasedOnName(userName);
        int totalAmountOfRewards = 0;
        for(UserReward s : theUser.getUserRewards()){
            totalAmountOfRewards += s.getRewardPoints();
        }
        return tripPricerProxy.getPrices("Hey", theUser.getUserId(), theUser.getUserPreferences().getNumberOfAdults(), theUser.getUserPreferences().getNumberOfChildren(), theUser.getUserPreferences().getTripDuration(), totalAmountOfRewards);
    }


    // **********************************************************************************************************************************************
    // This part correspond off all logic corresponding to the Rewards Domain (and therefore the link with the corresponding microservice RewardApp).
    // **********************************************************************************************************************************************

    /**
     *
     * <p>getAttractionRewardsPoints take as parameters the UUID of an attraction and of a user and returns the rewards points related.</p>
     *
     * @see RewardProxy
     * @param attraction
     *              The attraction UUID from which we want to generate rewards points.
     * @param user
     *              The UUID of the user that want to generate rewards points.
     *
     * @return
     */
    public int getAttractionRewardsPoints(UUID attraction, UUID user) {

        return rewardProxy.getTheReward(attraction, user);

    }

    /**
     * <p>calculateTheRewardsOfUser take a User as a parameter and a list of attraction.</p>
     * <p>It transform this information into a DTO in order to send solely the needed information's.</p>
     * <p>It send the information to the API via proxy in charge of calculating the rewards (in our case, RewardApp).</p>
     * <p>The proxy returns a DTO with updated UserRewards in correspondence with the rewarding policy set in the API.</p>
     * <p>It transforms the DTO back into a User and save the new information for the given user and return the given user.</p>
     *
     * @see UserReward
     * @see UserAndAttractionDTO
     * @param user
     *            The user (from the list of users) from whom we want to calculate the rewards.
     * @param attractions
     *             The list of all attractions that can generate rewards points.
     * @return
     */
    public User calculateTheRewardsOfUser(User user, List<Attraction> attractions){

        UserAndAttractionDTO sendingResult = transformUserIntoUserAndAttractionDTO(user, attractions);
        UserAndAttractionDTO updatedResultTwo = rewardProxy.calculateTheUserReward(sendingResult);
        User result = saveTheUserAndAttractionDTOreturnUser(updatedResultTwo);
        return result;
    }


    /**
     *
     * <p>getAllRewardsPointsOfUsers take a list of attraction as a parameter.</p>
     * <p>it transform the list of all users of the application into a corresponding list of DTO's in order to solely send required information's.</p>
     * <p>It send the list of DTO's to the API via the proxy (in our case, the API is RewardApp)</p>
     * <p>The proxy returns a list of DTO'S with updated UserRewards.</p>
     * <p>It transform the list of DTO's back into a UserType and save all the information provided back by the proxy.</p>
     * <p>It returns a Map composed of UserName and of all rewards related to the given user.</p>
     *
     * @param attractionList
     *             The list of all attraction that can generate rewards points.
     * @return Map<String, List<UserReward>
     */
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
    // ****************                        TRACKER domain                             ************************
    // ***********************************************************************************************************


    /**
     * <p>trackUserLocation centralized all the needed business logic of the application</p>
     * <p>It assembles all the previous capabilities in order to furnish the service to the Tracker.</p>
     * <p>It first send the list of all users to the API in charge of the localization (in our case, GpsUtil) via the proxy.</p>
     * <p>It then send the updated list of users (therefore with new visited location) to the API in charge of the calculus of rewards (in our case, RewardApp) via the proxy.</p>
     * <p>At each step, it transform the list of current users' of the application into the corresponding DTO (in order to solely send needed information) and save the updated results.</p>
     * <p>the method returns void because all the necessary logic (adding a new visited location to all users and check if a Rewards is related to it plus saving the updated data) is done in the previous methods.</p>
     *
     *
     * @see Tracker
     * @see GpsUtilProxy
     * @see RewardProxy
     *
     */
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


    /**
     *
     * <p>getTheUserBasedOnName is a utility method. It checks if the provided userName exists in the list of all the current users of the application.</p>
     * @param userName
     *              The user's name from whom we intend to find the corresponding User in the list of current users.
     * @return User
     */
    public User getTheUserBasedOnName(String userName) {

       Optional<User> result = users.stream().filter(n -> n.getUserName().equals(userName)).findAny();


        return result.orElse(null);
    }

    /**
     * <p>saveTheUserDTOAndReturnUser is a utility method. It takes a DTO as argument, update the corresponding user with the new information's (originated from the proxy) and save the new data's.</p>
     *
     * @param newResult
     *          The returned DTO from the API which we want to convert back to a User type.
     * @return User
     */
    public User saveTheUserDTOAndReturnUser(UserGpsDTO newResult) {

        User result = getTheUserBasedOnId(newResult.getUserId());
        int indexOfResult = users.indexOf(result);
        result.setVisitedLocations(newResult.getVisitedLocations());
        users.remove(indexOfResult);
        users.add(indexOfResult, result);
        return result;
    }


    /**
     * <p>getTheUserBasedOnId is a utility method. It checks if the provided UUID correspond to a user in the current users' of the application.</p>
     *
     * @param theId
     *          The UUID from which we intend to find a user corresponding.
     * @return User
     */
    public User getTheUserBasedOnId(UUID theId){

        Optional<User> result = users.stream().filter(n -> n.getUserId().equals(theId)).findAny();

        return result.orElse(null);

    }


    /**
     *
     * <p>transformUserIntoUserGpsDto is a utility method. It takes a User type as argument and transform it into a DTO.</p>
     * <p>The DTO in question (UserGpsDTO) centralize solely the needed information's of the API for the supply the related services.</p>
     * <p>It returns a ready to be sent DTO.</p>
     * @param theUser
     *              the User instance that we want to convert to a DTO to communicate with the API.
     * @return UserGpsDTO
     */
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

    /**
     * <p>saveTheUserAndAttractionDTOreturnUser is a utility method. It takes a UserAndAttractionDTO type as an argument (a dedicated type for the API in charge of the rewards).</p>
     * <p>It finds the corresponding user from the list of all users of the application.</p>
     * <p>It saves the updated user (with the data from the DTO) into the list of all users of the applications.</p>
     *
     * @param updatedResultTwo
     *        The result from the DTO that we want to convert back to a User Type.
     * @return User
     */
    public User saveTheUserAndAttractionDTOreturnUser(UserAndAttractionDTO updatedResultTwo) {


        User result = getTheUserBasedOnId(updatedResultTwo.getUserId());
        int indexOfResult = users.indexOf(result);
        result.setUserRewards(updatedResultTwo.getUserRewards());
        users.remove(indexOfResult);
        users.add(indexOfResult, result);
        return result;
    }


    /**
     *
     * <p>transformUserIntoUserAndAttractionDTO is a utility method. It takes a User type as argument and transform it into a DTO.</p>
     * <p>The DTO in question (UserAndAttractionDTO) centralize solely the needed information's of the API for the supply the related services.</p>
     * <p>It returns a ready to be sent DTO.</p>
     * @param newResult
     *              The instance of user that we want to convert into a DTO to send to the API.
     * @param attractions
     *              The list of all existing attractions from which we want to calculate rewards.
     * @return
     */
    public UserAndAttractionDTO transformUserIntoUserAndAttractionDTO(User newResult, List<Attraction> attractions) {

        UserAndAttractionDTO result = new UserAndAttractionDTO();
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




}
