package UserApp.proxy;

import UserApp.model.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name="TripPricerApp", url="localhost:8083")
public interface TripPricerProxy {

    @GetMapping("/getPrice?Apikey={api}&UserId={id}&adults={adults}&children={children}&duration={duration}&rewards={rewards}")
    List<Provider> getPrices(@PathVariable("api") String apikey, @PathVariable("id") UUID userId, @PathVariable("adults")int adults, @PathVariable("children")int children, @PathVariable("duration") int duration, @PathVariable("rewards")int cumulatedRewards);

}
