package TripPricerApplication.service;

import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;


/**
 * <p>TripPricerService is the service in charge of the business logic, making the link between the client (via the controller) and the library by providing added value.</p>
 * <p>The library is present for building the whole structure of the microservices of this project : once the application is validated, we can simply communicate with an online API instead of a library.</p>
 *
 */
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
