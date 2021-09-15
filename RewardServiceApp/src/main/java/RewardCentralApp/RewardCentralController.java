package RewardCentralApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private static Logger logger = LoggerFactory.getLogger(RewardCentralController.class);


    @GetMapping("/")
    public String getTheThing(){
        return "MDR";
    }

    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId")UUID attraction, @RequestParam("userId") UUID user){

        return rewardCentralService.getAttractionRewardPoints(attraction, user);
    }

}
