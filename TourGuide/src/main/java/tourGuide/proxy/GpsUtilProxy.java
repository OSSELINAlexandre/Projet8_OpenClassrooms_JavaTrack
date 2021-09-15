package tourGuide.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.dto.VisitedLocationTwo;

import java.util.UUID;

@FeignClient(name="gpsUtil", url="localhost:8081")
public interface GpsUtilProxy {

    @GetMapping("/getLocation?userName={x}")
    VisitedLocationTwo recuperateTheLocation(@PathVariable("x")UUID theUUID);
}
