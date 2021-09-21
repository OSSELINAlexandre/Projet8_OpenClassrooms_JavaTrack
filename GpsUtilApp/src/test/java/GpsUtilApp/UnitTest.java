package GpsUtilApp;

import GpsUtilApp.model.*;
import GpsUtilApp.proxy.RewardCentralProxy;
import GpsUtilApp.service.GpsUtilService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTest {

    @Autowired
    GpsUtilService gpsUtilService;

    @Mock
    RewardCentralProxy rewardCentralProxy;

    @Before
    public void init(){

        Locale.setDefault(Locale.ENGLISH);

    }


    @Test
    public void Test_getTheUserLocationTest(){

        VisitedLocation result = gpsUtilService.getTheUser(UUID.randomUUID());
        assertTrue(result != null);
    }

    @Test
    public void Test_getAllAttraction(){

        List<Attraction> result = gpsUtilService.getAllAttraction();
        assertTrue(result.size() == 26);

    }

    @Test
    public void Test_nearAttraction(){
        Attraction testAttraction = new Attraction("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D);
        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
        Boolean result = gpsUtilService.nearAttraction(testVisited, testAttraction);
        assertTrue(result);

    }

   @Test
    public void Test_getAllTheList(){

       Location testLocation = new Location(28.012804D, -82.469269D);
       VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
       User userA = new User(UUID.randomUUID(), "Jon", "000", "@@");
       User userB = new User(UUID.randomUUID(), "Robert", "000", "@@");
       User userC = new User(UUID.randomUUID(), "Camy", "000", "@@");

       userA.addToVisitedLocations(testVisited);
       userB.addToVisitedLocations(testVisited);
       userC.addToVisitedLocations(testVisited);

       List<User> users = new ArrayList<>();
       users.add(userA);
       users.add(userB);
       users.add(userC);

       List<Location> result = gpsUtilService.getAllTheList(users);

       assertTrue(result.size() == 3 && result.get(2) == testLocation && result.get(1) == testLocation && result.get(0)== testLocation);

   }


   @Test
    public void TEST_GetFifthClosestAttraction(){
       Attraction A = new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D);
       UUID uuidOfA = A.attractionId;
       Location testLocationA = new Location(42.302601D, -71.086731D);
       VisitedLocation testVisitedA = new VisitedLocation(UUID.randomUUID(), testLocationA, new Date());


       List<Location> resultListTest = new ArrayList<>();
       resultListTest.add(testLocationA);

       UUID x = UUID.randomUUID();
       User userA = new User(x, "Jon", "000", "@@");
       userA.addToVisitedLocations(testVisitedA);

       when(rewardCentralProxy.getTheReward(uuidOfA, x)).thenReturn(5);
       gpsUtilService.setTheReward(rewardCentralProxy);

       List<UserNearbyAttraction> result = gpsUtilService.getNearByFifthClosestAttractions(userA);

       List<Double> testingTheDistance = new ArrayList<>();


       // GETTING ALL THE DISTANCES POSSIBLE FROM THE GIVEN POINT
       for(Attraction a : gpsUtilService.getAllAttraction()){

           testingTheDistance.add(gpsUtilService.getDistance(a, userA.getLastVisitedLocation().location));
       }

        Double minA = Collections.min(testingTheDistance);
        testingTheDistance.remove(minA);
       Double minB = Collections.min(testingTheDistance);
       testingTheDistance.remove(minB);
       Double minC = Collections.min(testingTheDistance);
       testingTheDistance.remove(minC);
       Double minD = Collections.min(testingTheDistance);
       testingTheDistance.remove(minD);
       Double minE = Collections.min(testingTheDistance);
       testingTheDistance.remove(minE);

        List<Double> minimals = new ArrayList<>();
        minimals.add(minA);
        minimals.add(minB);
        minimals.add(minC);
        minimals.add(minD);
        minimals.add(minE);



        //ASSERT THAT ALL MINIMUM POINTS EXISTING IN THE LIST ARE ALSO IN THE RESULT OF THE USER;
       assertTrue(minimals.contains(result.get(0).getDistanceInMilesBetweenUserAndAttraction())
       && minimals.contains(result.get(1).getDistanceInMilesBetweenUserAndAttraction())
       && minimals.contains(result.get(2).getDistanceInMilesBetweenUserAndAttraction())
       && minimals.contains(result.get(3).getDistanceInMilesBetweenUserAndAttraction())
       && minimals.contains(result.get(4).getDistanceInMilesBetweenUserAndAttraction()));
    }

    @Test
    public void Test_calculateRewards(){


        Attraction A = new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D);
        UUID uuidOfA = A.attractionId;
        Location testLocationA = new Location(42.302601D, -71.086731D);
        VisitedLocation testVisitedA = new VisitedLocation(UUID.randomUUID(), testLocationA, new Date());
        UUID x = UUID.randomUUID();
        User userA = new User(x, "Jon", "000", "@@");
        userA.addToVisitedLocations(testVisitedA);

        gpsUtilService.calculateRewards(userA);
        assertTrue(userA.getUserRewards().size() != 0);

    }


}


