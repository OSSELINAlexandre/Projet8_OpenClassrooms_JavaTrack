package RewardCentralApp.controller;

import RewardCentralApp.model.User;
import RewardCentralApp.service.RewardCentralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private static Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    
    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId")UUID attraction, @RequestParam("userId") UUID user){

        Integer rewards = rewardCentralService.getAttractionRewardPoints(attraction, user);

        return rewards;

    }

    @PostMapping("/calculateReward")
    public User calculateTheRewardsForThisUser(@RequestBody User theUser){

        return rewardCentralService.calculateRewards(theUser);
    }

}
