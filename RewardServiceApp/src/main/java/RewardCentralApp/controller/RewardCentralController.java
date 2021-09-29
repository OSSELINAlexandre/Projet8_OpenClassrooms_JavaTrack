package RewardCentralApp.controller;

import RewardCentralApp.model.User;
import RewardCentralApp.service.RewardCentralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * <p>RewardCentralController centralizes all the endpoints of our microservices.</p>
 *
 *
 */
@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private static Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    /**
     *
     * <p>getTheReward generate the reward points associated to an attraction and a user.</p>
     *
     * @param attraction
     *          The UUID of a given attraction.
     * @param user
     *          The UUID of a given user.
     * @return int
     */
    @GetMapping("/getReward")
    public int getTheReward(@RequestParam("attId")UUID attraction, @RequestParam("userId") UUID user){


        logger.info("The User " + user.toString() + " has entered getTheReward ");

        Integer rewards = rewardCentralService.getAttractionRewardPoints(attraction, user);

        return rewards;

    }

    /**
     *
     * <p>calculateTheRewardsForThisUser calculate the reward of a given user. </p>
     *
     * @param theUser
     *          The user to whom we want to calculate the rewards.
     * @return User
     */
    @PostMapping("/calculateReward")
    public User calculateTheRewardsForThisUser(@RequestBody User theUser){

        logger.info("The User " + theUser.getUserName() + " has entered calculateReward ");


        return rewardCentralService.calculateRewards(theUser);
    }


    /**
     * <p>calculateTheRewardsForAllTheUsers is the 'principal' method of our application</p>
     * <p>Indeed, it's the method that is called by the User microservice in order to generate the rewards points.</p>
     *
     * @param theUsers
     *              The list of all users we want to calculate rewards from.
     * @return List<User>
     */
    @PostMapping("/calculateAllRewardsOfUsers")
    public List<User> calculateTheRewardsForAllTheUsers(@RequestBody List<User> theUsers){

        return rewardCentralService.calculateAllTheRewards(theUsers);
    }
}
