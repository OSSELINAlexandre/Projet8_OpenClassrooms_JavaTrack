package GpsUtilApp.controller;

import GpsUtilApp.model.*;
import GpsUtilApp.service.GpsUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>GpsUtilController centralizes all the endpoints of our microservices.</p>
 *
 *
 */
@RestController
public class GpsUtilController {

    private static Logger logger = LoggerFactory.getLogger(GpsUtilController.class);


    @Autowired
    GpsUtilService gpsUtilService;


    /**
     * <p>getTheLocationOfAUser request a User as body and call the service.</p>
     * <p>This method return a user type, updated with a visited location.</p>
     *
     *
     * @see VisitedLocation
     * @param theId
     *         The user from whom we want to have a last visited location.
     * @return User
     */
    @PostMapping("/getLocation")
    public User getTheLocationOfAUser(@RequestBody User theId){

        logger.info("The User " + theId.getUserName() + " has entered GetLocation ");
        User result = gpsUtilService.trackTheUser(theId);

        return result;

    }

    /**
     * <p>getTheAttraction is the endpoints that return the list of all available attractions from the library.</p>
     *
     * @see Attraction
     * @return getTheAttraction
     */
    @GetMapping("/getAttraction")
    public List<Attraction> getTheAttraction(){

        return gpsUtilService.getAllAttraction();
    }

    /**
     * <p>getNearbyAttractions is the endpoints that return the fifth closest attraction of the user.</p>
     * <p>The result depends on the last visited location of the user, which is an attribute of the requested body.</p>
     * @param user
     *          The user from whom we want to find out the five closest attractions.
     * @return List<UserNearbyAttraction>
     */
    @PostMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestBody User user){

        logger.info("The User " + user.getUserName() + " has entered getNearbyAttractions ");

        return gpsUtilService.getNearByFiveClosestAttractions(user);
    }


    /**
     * <p>getAllCurrentLocation is our main endpoint, with a list of users as a parameter, this endpoint will call the service in charge of 'searching' the new localization of users.</p>
     * <p>It returns a list of updated users with new last visited location. </p>
     *
     * @param users
     *          The list of users from whom you want to find new localizations.
     * @return List<User>
     */
    @PostMapping("/getAllCurrentLocations")
    public List<User> getAllCurrentLocation(@RequestBody List<User> users){


        List<User> Users = gpsUtilService.getAllTheList(users);



        return Users;
    }

}
