package UserApp;

import UserApp.model.Location;
import UserApp.model.User;
import UserApp.model.VisitedLocation;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import UserApp.testers.InternalUsersSetters;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest {


    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    @Autowired
    UserService userService;


    @Before
    public void init() {

        Locale.setDefault(Locale.ENGLISH);


    }

    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15
        // minutes
        InternalUsersSetters.setNumberOfWantedUsers(100);

        List<User> allUsers = new ArrayList<>();
        allUsers = userService.users;
        System.out.println("========================FCUK  " + allUsers.size());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            userService.trackUserLocation(user);
        }
        stopWatch.stop();
        userService.tracker.stopTracking();

        System.out.println("||||highVolumeTrackLocation: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


    @Test
    public void highVolumeGetRewards() {

        // Users should be incremented up to 100,000, and test finishes within 20
        // minutes
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());


        List<User> allUsers = userService.users;
        allUsers.forEach(u -> u.addToVisitedLocations(testVisited));

        allUsers.forEach(u -> userService.trackUserLocation(u));

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() == 26 );
        }
        stopWatch.stop();
        userService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


}
