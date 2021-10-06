package UserApp.Performances;

import UserApp.model.*;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class UserPerformanceTest {


    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    @Autowired
    UserService userService;



    @BeforeEach
    public void init() throws InterruptedException {
        Locale.setDefault(Locale.ENGLISH);

        Thread.currentThread().sleep(1000);

    }


    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15
        // minutes

        CopyOnWriteArrayList<User> usersTemp = new CopyOnWriteArrayList<>();

        Integer wantedOccurences = 1000;

        IntStream.range(0, wantedOccurences).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            usersTemp.add(user);
        });

        userService.users = usersTemp;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println(userService.users.size() + " highVolumeTrackLocation I don't wish them evil ");
        List<VisitedLocation> result = userService.getAllLocationOfUsers();
        System.out.println("highVolumeTrackLocation result size " + result.size());

        stopWatch.stop();

        assertTrue(result.size() == wantedOccurences);


        System.out.println("||||highVolumeTrackLocation: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }



    @Test
    public void highVolumeGetRewards() {


        // Users should be incremented up to 100,000, and test finishes within 20
        // minutes

        CopyOnWriteArrayList<User> usersTemp = new CopyOnWriteArrayList<>();
        Location testLocation = new Location(39.937778D, -82.40667D);
        List<Attraction> attractions = userService.getAllAttraction();

        Integer wantedOccurences = 1000;

        IntStream.range(0, wantedOccurences).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            VisitedLocation testVisitedLocation = new VisitedLocation(user.getUserId(), testLocation, new Date());
            user.addToVisitedLocations(testVisitedLocation);

            usersTemp.add(user);
        });

        userService.users = usersTemp;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        Map<String, List<UserReward>> result =  userService.getAllRewardsPointsOfUsers(attractions);

        stopWatch.stop();

        assertTrue(result.size() == wantedOccurences);

        for(Map.Entry<String, List<UserReward>> s : result.entrySet()){
            assertTrue(s.getValue().size() == 1);
        }


        System.out.println("Size of result from Rewards central : " + result.size());
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


}
