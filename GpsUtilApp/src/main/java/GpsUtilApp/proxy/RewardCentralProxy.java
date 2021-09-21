package GpsUtilApp.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="RewardCentralApp", url="localhost:8082")
public interface RewardCentralProxy {

    @GetMapping("/getReward?attId={x}&userId={y}")
    int getTheReward(@PathVariable("x")UUID AttractionId, @PathVariable("y")UUID UserId);
}
