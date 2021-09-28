package UserApp.controller;

import UserApp.dto.UserPreferencesDTO;
import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * <b>UserController is the controller of our 'Users' application.</b>
 *
 * <p>The controller centralize all the endpoints needed for the application to direct the client request to the proper service.</p>
 *
 */
@RestController
public class UserController {



    @Autowired
    UserService userService;

    @Autowired
    RewardProxy rewardProx;

    @Autowired
    GpsUtilProxy gpsProxy;

    // ***********************************************************************************************************
    // ************                            LIEE AU Users themselves                   ************************
    // ***********************************************************************************************************

    /**
     * <b>addUser request a User from the client and add it to the list of current user of the application.</b>
     *
     * @param userName
     * @return String
     */
    @PostMapping("/addUser")
    public String addAUser(@RequestBody User userName){

        User result = userService.addAUser(userName);

        if(result != null){

            return "Added user to the users of the application";

        }else{

            return "Could not add the user to the application, the user already exists.";
        }

    }

    /**
     * <b>deleteAUser request the name of a user of the application in order to delete it from the users' list.</b>
     *
     * @param userName
     * @return String
     */
    @GetMapping("/deleteUser")
    public String deleteAUser(@RequestParam("userName") String userName) {

        Boolean result = userService.deleteAUser(userName);

        if(result) {

            return "The user has been deleted.";

        }else{

            return "Could not delete the given user, does not exist in the list.";
        }

    }

    /**
     *
     * <b>updateThePreferences request a userName as parameter and new preferences as body and update the preferences of a user of the application</b>
     * <p>The preferences of a user are the registered preferences of a user .</p>
     * <p>The main function of preferences is to set a price for different trip by different travel agencies</p>
     *
     *
     * @see UserPreferences
     * @param user
     * @param userPref
     * @return User
     */
    @PostMapping("/updateUserPreference")
    public User updateThePreferences(@RequestParam("userName") String user, @RequestBody UserPreferencesDTO userPref){

        User result = userService.updateUserPreferences(user, userPref);

        if(result != null){

            return result;

        }else{

            return null;

        }


    }

    /**
     *
     * <p>getTheUserPreferences request a userName as parameter and send the preferences of the given user.</p>
     *
     * <p>It sends Null if the user does not exist in the application.</p>
     *
     * @see UserPreferences
     * @param user
     * @return UserPreferences
     */
    @GetMapping("/getUserPreference")
    public UserPreferences getTheUserPreference(@RequestParam("userName") String user){
        return userService.getUserPreference(user);
    }

    /**
     * <b>getTheRewards request a userName as parameter and send all the rewards for this given user.</b>
     * <p>It returns an empty list if the user has no rewards (this result is dependant on the API in charge of the rewards, in our case, RewardApp).</p>
     * <p>It returns null if the user does not exist in the application.</p>
     *
     *
     * @see UserReward
     * @param user
     * @return CopyOnWriteArrayList<UserReward>
     */
    @GetMapping("/getRewards")
    public CopyOnWriteArrayList<UserReward> getTheRewards(@RequestParam("userName") String user){
        return userService.getUserRewards(user);

    }

    /**
     * <p>getTheUser requests a UserName as parameter and return all the attributes of this given user.</p>
     * <p>It returns null if the user does not exist in the application.</p>
     *
     * @see User
     * @param user
     * @return User
     */
    @GetMapping("/getUser")
    public User getTheUser(@RequestParam("userName") String user){
        return userService.getASpecificUser(user);
    }


    /**
     *
     * <p>getAllTheLocationOfGivenUser request a userName as a parameter and returns all the visited location of this given user. </p>
     *
     * <p>It returns an empty list if no 'visitedLocation' has been added to the user (therefore if the 'tracker' hasn't yet provided the updated list of users to the others microservices.)</p>
     * <p>It returns null if the user does not exist in the application.</p>
     *
     * @see VisitedLocation
     * @param user
     * @return CopyOnWriteArrayList<VisitedLocation>
     */
    @GetMapping("/getAllLocationUser")
    public CopyOnWriteArrayList<VisitedLocation> getAllTheLocationOfGivenUser(@RequestParam("userName") String user){

        return userService.getAllUserLocationGivenUser(user);
    }


    // ***********************************************************************************************************
    // *****       All the below functionalities are related to the GpsUtillApplication microservice         *****
    // ***********************************************************************************************************

    /**
     *
     * <b>getTheLocationOfUser request as a parameter the name of a user and it returns the current
     * location of the  given user.</b>
     *
     *
     * <p>For coherence with the application, solely the tracker save a new visited location to a user.</p>
     * <p>Therefore getTheLocationOfUser will not update the given user with the 'VisitedLocation' sent back by this method.</p>
     *
     *
     * @see VisitedLocation
     * @param user
     * @return VisitedLocation
     */
    @GetMapping("/getLocation")
    public VisitedLocation getTheLocationOfUser(@RequestParam("userName") String user){

        return userService.getUserLocation(user);
    }

    /**
     *
     * <b>getAllTheAttractions returns a list of all the attractions provided by the API (GpsUtillApp in our case).</b>
     *
     *
     * @see Attraction
     * @return List<Attraction>
     */
    @GetMapping("/getAttraction")
    public List<Attraction> getAllTheAttractions(){

        return userService.getAllAttraction();
    }


    /**
     *
     * <p>getNearbyAttractions request a userName as a parameter,
     * and returns the fifth closest attractions to the last visited location of the given user (according to the attractions provided by the API).</p>
     * <p>It returns null if the user isn't registered to the application. </p>
     *
     * @see UserNearbyAttraction
     * @param userName
     * @return List<UserNearbyAttraction>
     */
    @GetMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestParam("userName") String userName) {


        return userService.getAllFifthClosestAttraction(userName);
    }


    /**
     *  <p>getAllTheLastLocationOfAllUsers returns a list of all the 'VisitedLocation' of all the users of the application.</p>
     *  <p>The 'VisitedLocation' has an attribute of the UserId, allowing the requester to analyze the data. </p>
     * @see VisitedLocation
     * @return
     */
    @GetMapping("/getAllCurrentLocations")
    public List<VisitedLocation> getAllTheLastLocationOfAllUsers(){

        return userService.getAllLocationOfUsers();
    }



    // ***********************************************************************************************************
    // *******       All the below functionalities are related to the TripApplication microservice         *******
    // ***********************************************************************************************************

    /**
     * <p>getTheDifferentTripDeals requests a userName as a parameter and finds all the offered deals in function of his preferences.</p>
     *
     *
     * @param userName
     * @return List<Provider>
     */
    @GetMapping("/getTripDeals")
    public List<Provider> getTheDifferentTripDeals(@RequestParam("userName") String userName){
        return userService.getAllTheDeals(userName);
    }

    // ***********************************************************************************************************
    // *******      All the below functionalities are related to the RewardApplication microservice        *******
    // ***********************************************************************************************************


    /**
     *
     * <p>getTheReward requests the UUID of an attraction and the UUID of a User and returns the rewards points related to it.</p>
     *
     * @param attraction
     * @param user
     * @return
     */
    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId") UUID attraction, @RequestParam("userId") UUID user){


        return userService.getAttractionRewardsPoints(attraction, user);
    }

    /**
     *
     * <p>findAllResultsOfUsers request a list of attraction as a body.</p>
     * <p>It will return a Map composed with all the users of the applications and the rewards points given to every user if have been attributed.</p>
     *
     *
     * @param attractionList
     * @return Map<String, List<UserReward>>
     */
    @PostMapping("/calculateAllRewardsOfUsers")
    public Map<String, List<UserReward>> findAllResultsOfUsers(@RequestBody List<Attraction> attractionList){

        return userService.getAllRewardsPointsOfUsers(attractionList);
    }




}
