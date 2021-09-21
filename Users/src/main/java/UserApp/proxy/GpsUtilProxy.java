package UserApp.proxy;

import UserApp.dto.UserDTO;
import UserApp.model.Attraction;
import UserApp.model.UserNearbyAttraction;
import UserApp.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="GpsUtilApp", url="localhost:8081")
public interface GpsUtilProxy {

    //TODO check if better be UUID or User
    @PostMapping("/getLocation")
    public UserDTO getTheLocation(@RequestBody UserDTO theUser);

    @GetMapping("/getAttraction")
    public List<Attraction> getAllAttraction();

    @GetMapping("/getNearbyAttractions?User={x}")
    public List<UserNearbyAttraction> getFifthClosestAttraction(@PathVariable UserDTO user);

    @PostMapping("/getAllCurrentLocations")
    public List<VisitedLocation> getAllLocationOfUsers(@RequestBody List<UserDTO> users);
}
