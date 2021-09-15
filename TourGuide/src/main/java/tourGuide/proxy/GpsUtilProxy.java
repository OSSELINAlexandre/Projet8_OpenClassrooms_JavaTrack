package tourGuide.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.dto.gpsutildto.Attraction;
import tourGuide.dto.gpsutildto.VisitedLocation;

import java.util.List;
import java.util.UUID;

@FeignClient(name="gpsUtil", url="localhost:8081")
public interface GpsUtilProxy {

    @GetMapping("/getLocation?userName={x}")
    VisitedLocation recuperateTheLocation(@PathVariable("x")UUID theUUID);

    @GetMapping("/getAttraction")
    List<Attraction> recuperateTheAttraction();
}
