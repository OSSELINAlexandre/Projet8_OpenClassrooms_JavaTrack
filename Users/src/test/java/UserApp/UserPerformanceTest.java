package UserApp;

import UserApp.model.Attraction;
import UserApp.model.Location;
import UserApp.model.User;
import UserApp.model.VisitedLocation;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPerformanceTest {


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

    @Ignore
    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15
        // minutes

        CopyOnWriteArrayList<User> usersTemp = new CopyOnWriteArrayList<>();

        IntStream.range(0, 1000).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourguide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);


            usersTemp.add(user);
        });

        userService.users = usersTemp;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : userService.users) {

            VisitedLocation result = userService.getUserLocation(user.getUserName());
            user.getVisitedLocations().add(result);

        }
        stopWatch.stop();
        for(User u : userService.users){

            assertTrue(u.getVisitedLocations().size() == 1 );
        }

        System.out.println("||||highVolumeTrackLocation: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Ignore
    @Test
    public void highVolumeGetRewards() {


        // Users should be incremented up to 100,000, and test finishes within 20
        // minutes

        CopyOnWriteArrayList<User> usersTemp = new CopyOnWriteArrayList<>();
        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
        List<Attraction> attractions = userService.getAllAttraction();

        IntStream.range(0, 50).forEach(i -> {
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


        userService.users.forEach(u -> userService.calculateTheRewardsOfUser(u, attractions));

        for (User user : userService.users) {
            assertTrue(user.getUserRewards().size() == 1 );
        }
        stopWatch.stop();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


}
