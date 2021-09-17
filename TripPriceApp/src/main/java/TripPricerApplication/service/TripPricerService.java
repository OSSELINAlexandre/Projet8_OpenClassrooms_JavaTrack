package TripPricerApplication.service;

import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

@Service
public class TripPricerService {

    private final TripPricer tripPricer;

    public TripPricerService(){

        this.tripPricer = new TripPricer();
    }

    public List<Provider> getTheList(String key, UUID userId, int adults, int children, int duration, int rewardcumulated) {

        return tripPricer.getPrice(key, userId, adults, children, duration, rewardcumulated);
    }
}
