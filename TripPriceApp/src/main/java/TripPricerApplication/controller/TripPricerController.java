package TripPricerApplication.controller;

import TripPricerApplication.Application;
import TripPricerApplication.service.TripPricerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class TripPricerController {

    @Autowired
    TripPricerService tripPricerService;


    @GetMapping("/getPrice")
    public List<Provider> getThePrices(@RequestParam("Apikey") String key, @RequestParam("UserId") UUID userId, @RequestParam("adults") int adults, @RequestParam("children") int children, @RequestParam("duration") int duration, @RequestParam("rewards") int rewardcumulated){

        Future<List<Provider>> rewards = Application.executorService.submit(() -> tripPricerService.getTheList(key, userId, adults, children, duration, rewardcumulated));

        try {
            List<Provider> result = rewards.get();
            return result;
        }catch(Exception e){

            return null;
        }
    }
}
