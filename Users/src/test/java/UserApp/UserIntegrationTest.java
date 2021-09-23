package UserApp;

import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserIntegrationTest {

   /*
    ==============================================================================================================

                       Integration tests, therefore the containers with the apps need to be lauch.

    ==============================================================================================================

    */
    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    @Autowired
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
    }

    @Test
    public void integrationTest_getUserLocation(){

        userService.users.add(user);


        VisitedLocation result = userService.getUserLocation(user.getUserName());

        assertTrue(result != null);

    }

    @Test
    public void integrationTest_getAllAttraction(){


        List<Attraction> result = userService.getAllAttraction();

        assertTrue(result != null);

    }

    @Test
    public void integrationTest_getAllFifthClosestAttraction(){

        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(28.012804D, -82.469269D), new Date()));

        userService.users.add(user);

        List<UserNearbyAttraction> result = userService.getAllFifthClosestAttraction(user.getUserName());

        assertTrue(result != null);

    }

    @Test
    public void integrationTest_getAttractionRewardsPoints(){

        Integer result = userService.getAttractionRewardsPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result != 0);
    }

    @Test
    public void integrationTest_CalculateTheRewardsOfUser(){

        userService.setRewardProxy(rewardProxy);

        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        User userTest = new User(UUID.randomUUID(), userName, phone, email);

        userTest.addToVisitedLocations(new VisitedLocation(userTest.getUserId(), new Location(28.012804D, -82.469269D), new Date()));

        userService.users.add(userTest);

        User result = userService.calculateTheRewardsOfUser(userTest, gpsUtilProxy.getAllAttraction());
        assertTrue(result.getUserRewards().get(0).getRewardPoints() != 0);
    }

}
