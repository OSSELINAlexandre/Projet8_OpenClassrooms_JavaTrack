package UserApp.proxy;

import UserApp.model.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;
/**
 *
 * <p>The TripPricerProxy is an interface that centralizes all the signature of the endpoints for our API in charge of the trips (in our case, TripApp).</p>
 * <p>This interface makes the link thanks to OpenFeign library (see the gradle.build ). </p>
 * <p>The FeignClient annotation, thanks to an URL and an application name finds the microservice in question.</p>
 * <p>The signature methods are therefore available in our app, and will call the referenced microservice in order to obtain the results.</p>
 * <p>Of course, the completes methods exist in the API.</p>
 *
 * <p>For facilitation purposes, we have two addresses (one need to be commented) </p>
 * <ul>
 *     <li>Use the first one if your run the app with the DockerCompose.</li>
 *     <li>Use the second one if you run the app without DockerCompose.</li>
 * </ul>
 *
 * <p>If you launch the app with the java -jar, no further modifications need to be done.</p>
 * <p>If you use docker, you still use the second url (localhost), but you need to set the ports in the docker run with -p 8083:8083</p>
 */
//@FeignClient(name="TripPricerApp", url="http://tripapp:8083")
@FeignClient(name="TripPricerApp", url="localhost:8083")
public interface TripPricerProxy {

    @GetMapping("/getPrice?Apikey={api}&userId={id}&adults={adults}&children={children}&duration={duration}&rewards={rewards}")
    List<Provider> getPrices(@PathVariable("api") String apikey, @PathVariable("id") UUID userId, @PathVariable("adults")int adults, @PathVariable("children")int children, @PathVariable("duration") int duration, @PathVariable("rewards")int cumulatedRewards);

}
