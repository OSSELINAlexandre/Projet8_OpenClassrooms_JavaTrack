package RewardCentralApp.controller;

import RewardCentralApp.Application;
import RewardCentralApp.service.RewardCentralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private static Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    
    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId")UUID attraction, @RequestParam("userId") UUID user){

        Future<Integer> rewards = Application.executorService.submit(() -> rewardCentralService.getAttractionRewardPoints(attraction, user));

        try {

            int rewardsResult = rewards.get();
            return rewardsResult;

        }catch(Exception e){

            return 0;
        }
    }

}
