package GpsUtilApp.controller;

import GpsUtilApp.Application;
import GpsUtilApp.model.Attraction;
import GpsUtilApp.model.Location;
import GpsUtilApp.model.User;
import GpsUtilApp.model.UserNearbyAttraction;
import GpsUtilApp.service.GpsUtilService;
import com.jsoniter.output.JsonStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class GpsUtilController {

    private static Logger logger = LoggerFactory.getLogger(GpsUtilController.class);


    @Autowired
    GpsUtilService gpsUtilService;


    @PostMapping("/getLocation")
    public User getTheCrapingLocation(@RequestBody User theId){

        User result = gpsUtilService.trackTheUser(theId);

        return result;

    }

    @GetMapping("/getAttraction")
    public List<Attraction> getTheAttraction(){

        return gpsUtilService.getAllAttraction();
    }

    @PostMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getNearbyAttractions(@RequestBody User user){


        return gpsUtilService.getNearByFifthClosestAttractions(user);
    }


    @PostMapping("/getAllCurrentLocations")
    public List<Location> getAllCurrentLocation(@RequestBody List<User> users){

        List<Location> visitedLocations = gpsUtilService.getAllTheList(users);

        return visitedLocations;
    }

}
