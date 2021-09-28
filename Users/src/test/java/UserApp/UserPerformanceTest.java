package UserApp;

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
    public void init() {
        userService.setTheProfileTrueForTestFalseForExperience(true);
        Locale.setDefault(Locale.ENGLISH);


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

        List<VisitedLocation> result = userService.getAllLocationOfUsers();

        stopWatch.stop();

        System.out.println("What is the size of it ???? " + result.size());
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
        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
        List<Attraction> attractions = userService.getAllAttraction();

        Integer wantedOccurences = 100000;


        IntStream.range(0, wantedOccurences).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            user.addToVisitedLocations(testVisited);
            usersTemp.add(user);
        });

        userService.users = usersTemp;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        Map<String, List<UserReward>> result =  userService.getAllRewardsPointsOfUsers(attractions);

        assertTrue(result.size() == wantedOccurences);
        stopWatch.stop();
        System.out.println("Size of result from Rewards central : " + result.size());
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


}
