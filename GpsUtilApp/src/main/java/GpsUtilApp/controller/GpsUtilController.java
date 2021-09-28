package GpsUtilApp.controller;

import GpsUtilApp.model.*;
import GpsUtilApp.service.GpsUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GpsUtilController {

    private static Logger logger = LoggerFactory.getLogger(GpsUtilController.class);


    @Autowired
    GpsUtilService gpsUtilService;


    @PostMapping("/getLocation")
    public User getTheLocationOfAUser(@RequestBody User theId){

        logger.info("The User " + theId.getUserName() + " has entered GetLocation ");
        User result = gpsUtilService.trackTheUser(theId);

        return result;

    }

    @GetMapping("/getAttraction")
    public List<Attraction> getTheAttraction(){

        return gpsUtilService.getAllAttraction();
    }

    @PostMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestBody User user){

        logger.info("The User " + user.getUserName() + " has entered getNearbyAttractions ");

        return gpsUtilService.getNearByFifthClosestAttractions(user);
    }


    @PostMapping("/getAllCurrentLocations")
    public List<User> getAllCurrentLocation(@RequestBody List<User> users){


        List<User> Users = gpsUtilService.getAllTheList(users);



        return Users;
    }

}
