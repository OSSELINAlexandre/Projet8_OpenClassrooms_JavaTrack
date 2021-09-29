package UserApp.proxy;

import UserApp.dto.UserGpsDTO;
import UserApp.model.Attraction;
import UserApp.model.UserNearbyAttraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * <p>The GpsUtilProxy is an interface that centralizes all the signature of the endpoints for our API in charge of the localization (in our case, GpsUtilApp).</p>
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
 * <p>If you use docker, you still use the second url (localhost), but you need to set the ports in the docker run with -p 8081:8081.</p>
 */
@FeignClient(name="GpsUtilApp", url="http://gpsutilapp:8081")
//@FeignClient(name="GpsUtilApp", url="localhost:8081")
public interface GpsUtilProxy {

    //TODO check if better be UUID or User
    @PostMapping("/getLocation")
    public UserGpsDTO getTheLocation(@RequestBody UserGpsDTO theUser);

    @GetMapping("/getAttraction")
    public List<Attraction> getAllAttraction();

    @PostMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getFiveClosestAttraction(@RequestBody UserGpsDTO user);

    @PostMapping("/getAllCurrentLocations")
    public List<UserGpsDTO> getAllLocationOfUsers(@RequestBody List<UserGpsDTO> users);
}
