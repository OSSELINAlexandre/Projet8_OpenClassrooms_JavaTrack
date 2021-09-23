package UserApp.proxy;

import UserApp.dto.UserGpsDTO;
import UserApp.model.Attraction;
import UserApp.model.UserNearbyAttraction;
import UserApp.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="GpsUtilApp", url="http://gpsutilapp:8081")
public interface GpsUtilProxy {

    //TODO check if better be UUID or User
    @PostMapping("/getLocation")
    public UserGpsDTO getTheLocation(@RequestBody UserGpsDTO theUser);

    @GetMapping("/getAttraction")
    public List<Attraction> getAllAttraction();

    @PostMapping("/getNearbyAttractions")
    public List<UserNearbyAttraction> getFifthClosestAttraction(@RequestBody UserGpsDTO user);

    @PostMapping("/getAllCurrentLocations")
    public List<VisitedLocation> getAllLocationOfUsers(@RequestBody List<UserGpsDTO> users);
}
