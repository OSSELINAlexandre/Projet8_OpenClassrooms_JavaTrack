package UserApp;


import UserApp.dto.UserAndAttractionDTO;
import UserApp.model.Attraction;
import UserApp.model.User;
import UserApp.model.UserReward;
import UserApp.model.VisitedLocation;
import UserApp.proxy.RewardProxy;
import UserApp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RewadsAppUnitTest {

    @Mock
    RewardProxy rewardProxy;

    @Autowired
    UserService userService;

    User user;

    @Before
    public void init(){
        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        user = new User(UUID.randomUUID(), userName, phone, email);
        user.addToVisitedLocations(new VisitedLocation());
        user.addToVisitedLocations(new VisitedLocation());
    }

    @Test
    public void unitTest_getAttractionRewardsPoints(){

        when(rewardProxy.getTheReward(any(), any())).thenReturn(50);

        userService.setRewardProxy(rewardProxy);

        int result = userService.getAttractionRewardsPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result == 50);
    }

    @Test
    public void unitTest_CalculateTheRewardsOfUser(){

        userService.users.add(user);

        UserAndAttractionDTO intermediaryResult = new UserAndAttractionDTO();
        intermediaryResult.setUserId(user.getUserId());
        UserReward intermed = new UserReward();
        intermed.setRewardPoints(150);
        CopyOnWriteArrayList<UserReward> testing = new CopyOnWriteArrayList<>();
        testing.add(intermed);
        intermediaryResult.setUserRewards(testing);

        when(rewardProxy.calculateTheUserReward(any())).thenReturn(intermediaryResult);
        userService.setRewardProxy(rewardProxy);

        User result = userService.calculateTheRewardsOfUser(user, new ArrayList<Attraction>());

        assertTrue(result.getUserRewards().get(0).getRewardPoints() == 150);
    }

}
