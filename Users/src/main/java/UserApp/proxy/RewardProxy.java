package UserApp.proxy;


import UserApp.dto.UserAndAttractionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>The RewardProxy is an interface that centralizes all the signature of the endpoints for our API in charge of the rewards (in our case, RewardApp).</p>
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
 * <p>If you use docker, you still use the second url (localhost), but you need to set the ports in the docker run with -p 8082:8082</p>
 */
@FeignClient(name="RewardCentralApp", url="localhost:8082")
//@FeignClient(name="RewardCentralApp", url="http://rewardapp:8082")
public interface RewardProxy {

    @GetMapping("/getReward?attId={x}&userId={y}")
    int getTheReward(@PathVariable("x") UUID AttractionId, @PathVariable("y")UUID UserId);

    @PostMapping("/calculateReward")
    UserAndAttractionDTO calculateTheUserReward(@RequestBody UserAndAttractionDTO user);

    @PostMapping("/calculateAllRewardsOfUsers")
    List<UserAndAttractionDTO> calculateTheRewardsForAllTheUsers(@RequestBody List<UserAndAttractionDTO> users);
}
