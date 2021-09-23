package UserApp.proxy;


import UserApp.dto.UserAndAttractionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name="RewardCentralApp", url="localhost:8082")
public interface RewardProxy {

    @GetMapping("/getReward?attId={x}&userId={y}")
    int getTheReward(@PathVariable("x") UUID AttractionId, @PathVariable("y")UUID UserId);

    @PostMapping("/calculateReward")
    UserAndAttractionDTO calculateTheUserReward(@RequestBody UserAndAttractionDTO user);
}
