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

    @PostMapping("/addUser")
    public String addAUser(@RequestBody User userName){

        User result = userService.addAUser(userName);

        if(result != null){

            return "Added user to the users of the application";

        }else{

            return "Could not add the user to the application, the user already exists.";
        }

    }

    @GetMapping("/deleteUser")
    public String deleteAUser(@RequestParam("userName") String userName) {

        Boolean result = userService.deleteAUser(userName);

        if(result) {

            return "The user has been deleted.";

        }else{

            return "Could not delete the given user, does not exist in the list.";
        }

    }

    @PostMapping("/updateUserPreference")
    public User updateThePreferences(@RequestParam("userName") String user, @RequestBody UserPreferencesDTO userPref){

        User result = userService.updateUserPreferences(user, userPref);

        if(result != null){

            return result;

        }else{

            return null;

        }


    }

    @GetMapping("/getUserPreference")
    public UserPreferences getTheUserPreference(@RequestParam("userName") String user){
        return userService.getUserPreference(user);
    }

    @GetMapping("/getRewards")
    public CopyOnWriteArrayList<UserReward> getTheRewards(@RequestParam("userName") String user){
        return userService.getUserRewards(user);

    }

    @GetMapping("/getUser")
    public User getTheUser(@RequestParam("userName") String user){
        return userService.getASpecificUser(user);
    }


    @GetMapping("/getAllLocationUser")
    public CopyOnWriteArrayList<VisitedLocation> getAllTheLocationOfGivenUser(@RequestParam("userName") String user){

        return userService.getAllUserLocationGivenUser(user);
    }


    // ***********************************************************************************************************
    // ************                            LIEE AU GpsUtilProxy                       ************************
    // ***********************************************************************************************************

    @GetMapping("/getLocation")
    public VisitedLocation getTheLocationOfUser(@RequestParam("userName") String user){

        return userService.getUserLocation(user);
    }

    @GetMapping("/getAttraction")
    public List<Attraction> getAllTheAttractions(){

        return userService.getAllAttraction();
    }


    @GetMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestParam("userName") String userName) {


        return userService.getAllFifthClosestAttraction(userName);
    }


    @GetMapping("/getAllCurrentLocations")
    public List<VisitedLocation> getAllTheLastLocationOfAllUsers(){

        return userService.getAllLocationOfUsers();
    }



    // ***********************************************************************************************************
    // ************                            LIEE AU TripPricerProxy                    ************************
    // ***********************************************************************************************************
    @GetMapping("/getTripDeals")
    public List<Provider> getTheDifferentTripDeals(@RequestParam("userName") String userName){
        return userService.getAllTheDeals(userName);
    }

    // ***********************************************************************************************************
    // ************                            LIEE AU RewardProxy                    ************************
    // ***********************************************************************************************************


    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId") UUID attraction, @RequestParam("userId") UUID user){


        return userService.getAttractionRewardsPoints(attraction, user);
    }

    @PostMapping("/calculateAllRewardsOfUsers")
    public Map<String, List<UserReward>> findAllResultsOfUsers(@RequestBody List<Attraction> attractionList){

        return userService.getAllRewardsPointsOfUsers(attractionList);
    }




}
