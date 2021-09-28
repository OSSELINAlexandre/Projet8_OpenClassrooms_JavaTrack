package RewardCentralApp;


import RewardCentralApp.model.Attraction;
import RewardCentralApp.model.Location;
import RewardCentralApp.model.User;
import RewardCentralApp.model.VisitedLocation;
import RewardCentralApp.service.RewardCentralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class UnitTest {



    @Autowired
    RewardCentralService rewardCentralService;

    @BeforeEach
    public void init(){

        rewardCentralService.distanceRequired = Integer.MAX_VALUE;
    }

    @Test
    public void getTheRewardFromService(){

        int result = rewardCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result != 0);

    }

    @Test
    public void Test_calculateRewards_ShouldCalculateRewardsWithNoDuplicates(){


        Attraction A = new Attraction("Kansas City Zoo", "Kansas City", "MO", 39.007504D, -94.529625D);
        Attraction B = new Attraction("Bronx Zoo", "Bronx", "NY", 40.852905D, -73.872971D);
        Attraction C = new Attraction("Cinderella Castle", "Orlando", "FL", 28.419411D, -81.5812D);
        Attraction D = new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D);
        UUID uuidOfA = A.attractionId;
        List<Attraction> testingItem = new ArrayList<>();
        testingItem.add(A);
        testingItem.add(B);
        testingItem.add(C);
        testingItem.add(D);

        Location testLocationA = new Location(42.302601D, -71.086731D);
        VisitedLocation testVisitedA = new VisitedLocation(UUID.randomUUID(), testLocationA, new Date());
        UUID x = UUID.randomUUID();
        User userA = new User(x, "Jon", "000", "@@");
        userA.setAttractions(testingItem);
        userA.setVisitedLocations(new CopyOnWriteArrayList<>());
        userA.setUserRewards(new CopyOnWriteArrayList<>());
        userA.addToVisitedLocations(testVisitedA);
        userA.addToVisitedLocations(testVisitedA);

        rewardCentralService.calculateRewards(userA);
        rewardCentralService.calculateRewards(userA);

        assertTrue(userA.getUserRewards().size() == 4);

    }

    @Test
    public void Test_calculateAllTheRewards(){
        Attraction A = new Attraction("Kansas City Zoo", "Kansas City", "MO", 39.007504D, -94.529625D);
        Attraction B = new Attraction("Bronx Zoo", "Bronx", "NY", 40.852905D, -73.872971D);
        Attraction C = new Attraction("Cinderella Castle", "Orlando", "FL", 28.419411D, -81.5812D);
        Attraction D = new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D);
        List<Attraction> testingItem = new ArrayList<>();
        testingItem.add(A);
        testingItem.add(B);
        testingItem.add(C);
        testingItem.add(D);

        Location testLocation = new Location(42.302601D, -71.086731D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());

        List<User> toBeTestedList = new ArrayList<>();

        UUID x = UUID.randomUUID();
        User userA = new User(x, "Jon", "000", "@@");
        userA.setAttractions(testingItem);
        userA.setVisitedLocations(new CopyOnWriteArrayList<>());
        userA.setUserRewards(new CopyOnWriteArrayList<>());
        userA.addToVisitedLocations(testVisited);


        UUID y = UUID.randomUUID();
        User userB = new User(y, "Robert", "000", "@@");
        userB.setAttractions(testingItem);
        userB.setVisitedLocations(new CopyOnWriteArrayList<>());
        userB.setUserRewards(new CopyOnWriteArrayList<>());
        userB.addToVisitedLocations(testVisited);




        UUID z = UUID.randomUUID();
        User userC = new User(z, "Julia", "000", "@@");
        userC.setAttractions(testingItem);
        userC.setVisitedLocations(new CopyOnWriteArrayList<>());
        userC.setUserRewards(new CopyOnWriteArrayList<>());
        userC.addToVisitedLocations(testVisited);


        toBeTestedList.add(userA);
        toBeTestedList.add(userB);
        toBeTestedList.add(userC);


        List<User> result = rewardCentralService.calculateAllTheRewards(toBeTestedList);

        for(User u : result){

            assertTrue(u.getUserRewards().size() == 4);

        }
    }


    @Test
    public void Test_nearAttraction(){
        Attraction testAttraction = new Attraction("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D);
        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
        Boolean result = rewardCentralService.nearAttraction(testVisited, testAttraction);
        assertTrue(result);

    }
}
