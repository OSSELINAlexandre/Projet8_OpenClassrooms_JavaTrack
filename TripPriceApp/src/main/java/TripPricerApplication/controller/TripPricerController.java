package TripPricerApplication.controller;

import TripPricerApplication.service.TripPricerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

/**
 * <p>TripPricerController is the controller in charge of routing the client request to the proper service.</p>
 *
 */
@RestController
public class TripPricerController {

    private Logger logger = LoggerFactory.getLogger(TripPricerController.class);


    @Autowired
    TripPricerService tripPricerService;


    /**
     *
     * <p>getThePrice generate a price depending on different attributes.</p>
     *
     *
     * @param key
     *          The API key.
     * @param userId
     *          The Id of the user to whom we want to obtain a price.
     * @param adults
     *          The number of adults.
     * @param children
     *          The number of children.
     * @param duration
     *          The trip duration.
     * @param rewardcumulated
     *          The cumulated rewards.
     * @return List<Provider>
     */
    @GetMapping("/getPrice")
    public List<Provider> getThePrices(@RequestParam("Apikey") String key, @RequestParam("userId") UUID userId, @RequestParam("adults") int adults, @RequestParam("children") int children, @RequestParam("duration") int duration, @RequestParam("rewards") int rewardcumulated){

        logger.info("getPrice from TripPriceApp has been called");
        
        List<Provider> rewards = tripPricerService.getTheList(key, userId, adults, children, duration, rewardcumulated);


        return rewards;

    }
}
