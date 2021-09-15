package GpsUtilApp;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GpsUtilController {

    private static Logger logger = LoggerFactory.getLogger(GpsUtilController.class);


    @Autowired
    GpsUtilService gpsUtilService;

    @GetMapping("/lol")
    public String getTheLol(){

        return "MDR";
    }

    @GetMapping("/UserLocation")
    public VisitedLocation getTheCrapingLocation(@RequestParam("userName") UUID theId){

        logger.info("Been there, just keep faith, be strong and courageous");

        return gpsUtilService.getTheUser(theId);

    }

}
