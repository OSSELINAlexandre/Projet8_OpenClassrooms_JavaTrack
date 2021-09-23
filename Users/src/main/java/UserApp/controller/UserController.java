package UserApp.controller;

import UserApp.dto.UserPreferencesDTO;
import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class UserController {


    private Logger logger = LoggerFactory.getLogger(UserController.class);


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
    public User addAUser(@RequestBody User userName){

        logger.info("======= Do we get here ? ");
        return userService.addAUser(userName);

    }

    @GetMapping("/deleteUser")
    public String deleteAUser(@RequestParam("userName") String userName){

        Boolean result = userService.deleteAUser(userName);

        if(result) {
            return "Deleted";
        }else{
            return "Couldn't delete the given user";
        }

    }

    @PostMapping("/updateUserPreference")
    public User updateThePreferences(@RequestParam("userName") String user, @RequestBody UserPreferencesDTO userPref){

        return userService.updateUserPreferences(user, userPref);

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

        return userService.getAllLastLocationUsers();
    }



    // ***********************************************************************************************************
    // ************                            LIEE AU TripPricerProxy                    ************************
    // ***********************************************************************************************************
    @GetMapping("/getTripDeals")
    public List<Provider> getTheDifferentTripDeals(@RequestParam String userName){
        return userService.getAllTheDeals(userName);
    }

    // ***********************************************************************************************************
    // ************                            LIEE AU RewardProxy                    ************************
    // ***********************************************************************************************************


    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId") UUID attraction, @RequestParam("userId") UUID user){


        return userService.getAttractionRewardsPoints(attraction, user);
    }




}
