package GpsUtilApp.controller;

import GpsUtilApp.Application;
import GpsUtilApp.service.GpsUtilService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class GpsUtilController {

    private static Logger logger = LoggerFactory.getLogger(GpsUtilController.class);


    @Autowired
    GpsUtilService gpsUtilService;


    @GetMapping("/getLocation")
    public VisitedLocation getTheCrapingLocation(@RequestParam("userName") UUID theId){

        Future<VisitedLocation> futureVisited = Application.executorService.submit(() -> gpsUtilService.getTheUser(theId));
        logger.info("Been there, just keep faith, be strong and courageous");
        try{

            VisitedLocation result = futureVisited.get();
            return result;
        }catch(Exception e){

            return null;
        }


    }

    @GetMapping("/getAttraction")
    public List<Attraction> getTheAttraction(){

        Future<List<Attraction>> listOfAllFuture = Application.executorService.submit(() -> gpsUtilService.getAllAttraction());

        try{
            List<Attraction> result = listOfAllFuture.get();
            return null;

        }catch(Exception e){

            return null;
        }

    }

}
