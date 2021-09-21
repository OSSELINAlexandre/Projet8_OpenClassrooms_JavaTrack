package UserApp.controller;

import UserApp.dto.UserPreferencesDTO;
import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.service.UserService;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.money.Monetary;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class UserController {


    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserService userService;

    @Autowired
    GpsUtilProxy TE;


    /*
    *
    * Endpoints related to the User itself
    *
    *
    * */

    @GetMapping("/hey")
    public VisitedLocation poilo(){


        VisitedLocation lol = userService.trackUserLocation(userService.users.get(2));

        return lol;
    }

    @GetMapping("/test")
    public UserPreferences lol(){

        UserPreferences newLol = new UserPreferences();
        newLol.setAttractionProximity(150);
        newLol.setLowerPricePoint(Money.of(1500, "USD"));
        newLol.setHighPricePoint(Money.of(1750, "USD"));
        newLol.setNumberOfAdults(15000);
        newLol.setNumberOfChildren(450);
        newLol.setTicketQuantity(750);
        newLol.setTripDuration(850);

        return newLol;
    }

    @PostMapping("/again")
    public UserPreferences polo(@RequestParam("currency") String currency, @RequestParam("numberAdults") int number){

        UserPreferences lol = new UserPreferences();
        lol.setCurrency(Monetary.getCurrency(currency));
        lol.setLowerPricePoint(Money.of(lol.getLowerPricePoint().getNumber(), lol.getCurrency()));
        lol.setHighPricePoint(Money.of(lol.getHighPricePoint().getNumber(), lol.getCurrency()));
        lol.setNumberOfAdults(number);
        return lol;

    }

    @PostMapping("/testing")
    public void mdr(@RequestBody UserPreferences lol){

        logger.info(lol.toString());

    }

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


    /*
    *
    * NotWorking if it's with a JsonStream.I think i will use Jackson instead in the coming thing to see why.
    * */
    @GetMapping("/getUserPreference")
    public UserPreferences getTheUserPreference(@RequestParam("userName") String user){
        return userService.getUserPreference(user);
    }

    @GetMapping("/getRewards")
    public CopyOnWriteArrayList<UserReward> getTheRewards(@RequestParam("userName") String user){
        logger.info("WE ARE IN THE CONTROLLER, Should be allright here");
        return userService.getUserRewards(user);

    }

    @GetMapping("/getUser")
    public User getTheUser(@RequestParam("userName") String user){

        return userService.getASpecificUser(user);
    }

    /*
    *
    * RelatedToTheApplicationItself
    *
    *
    * */

    @GetMapping("/getLocation")
    public VisitedLocation getTheLocationOfUser(@RequestParam("UserName") String user){

        return userService.getUserLocation(user);
    }

    @GetMapping("/getAllLocationUser")
    public CopyOnWriteArrayList<VisitedLocation> getAllTheLocationOfGivenUser(@RequestParam("UserName") String user){

        return userService.getAllUserLocationGivenUser(user);
    }

    @GetMapping("/getAttraction")
    public List<Attraction> getAllTheAttractions(){

        return userService.getAllAttraction();
    }

    @GetMapping("/getLastLocationOfAllUsers")
    public List<VisitedLocation> getAllTheLastLocationOfAllUsers(){

        return userService.getAllLastLocationUsers();
    }

    @GetMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestParam("UserName") String userName) {


        return userService.getAllFifthClosestAttraction(userName);
    }

    @GetMapping("/getTripDeals")
    public List<Provider> getTheDifferentTripDeals(@RequestParam String userName){
        return userService.getAllTheDeals(userName);
    }


}
