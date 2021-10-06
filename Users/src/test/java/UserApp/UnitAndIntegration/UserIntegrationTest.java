package UserApp.UnitAndIntegration;

import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    public void init() throws InterruptedException {

        Thread.currentThread().sleep(1500);

        userService.setGpsUtilProxy(gpsUtilProxy);
        userService.setRewardProxy(rewardProxy);
        userService.setTripPricerProxy(tripPricerProxy);

        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        user = new User(UUID.randomUUID(), userName, phone, email);
    }

    @Test
    public void IntegrationTest_getAllFifthClosestAttraction() throws InterruptedException {


        String userName = "TestUserA";
        String phone = "007";
        String email = userName + "@tourguide.com";
        User userTest = new User(UUID.randomUUID(), userName, phone, email);
        userTest.addToVisitedLocations(new VisitedLocation(userTest.getUserId(), new Location(28.012804D, -82.469269D), new Date()));
        userService.addAUser(userTest);


        Thread.currentThread().sleep(1500);
        List<UserNearbyAttraction> result = userService.getAllFiveClosestAttraction(userTest.getUserName());

        assertTrue(result != null);

    }

    @Test
    public void IntegrationTest_getUserLocation(){

        userService.users.add(user);


        VisitedLocation result = userService.getUserLocation(user.getUserName());

        assertTrue(result != null);

    }

    @Test
    public void IntegrationTest_getAllAttraction(){


        List<Attraction> result = userService.getAllAttraction();

        assertTrue(result != null);

    }




    @Test
    public void IntegrationTest_getAllLastLocationUsers(){

        CopyOnWriteArrayList<User> usersTemp = new CopyOnWriteArrayList<>();

        IntStream.range(0, 5).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);

            usersTemp.add(user);
        });

        userService.users = usersTemp;


        List<VisitedLocation> result = userService.getAllLocationOfUsers();

        assertTrue(result.size() == 5);


    }

    @Test
    public void IntegrationTest_getAttractionRewardsPoints(){

        Integer result = userService.getAttractionRewardsPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result != 0);
    }

    @Test
    public void IntegrationTest_CalculateTheRewardsOfUser(){

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
