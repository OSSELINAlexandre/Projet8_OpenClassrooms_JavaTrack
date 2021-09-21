package RewardCentralApp;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTest {



/*    @Autowired
    RewardCentralService rewardCentralService;

    @Test
    public void getTheRewardFromService(){

        int result = rewardCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result != 0);

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

    @Test
    public void Test_nearAttraction(){
        Attraction testAttraction = new Attraction("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D);
        Location testLocation = new Location(28.012804D, -82.469269D);
        VisitedLocation testVisited = new VisitedLocation(UUID.randomUUID(), testLocation, new Date());
        Boolean result = gpsUtilService.nearAttraction(testVisited, testAttraction);
        assertTrue(result);

    }*/
}
