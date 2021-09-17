package tourGuide;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.dto.gpsutildto.Attraction;
import tourGuide.dto.gpsutildto.Location;
import tourGuide.dto.gpsutildto.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRewardsService {

	@Autowired
	private RewardsService rewardsService ;

	@Autowired
	private TourGuideService tourGuideService;

	private User user;

	@Autowired
	GpsUtilProxy gpsUtilProxy;

	@Autowired
	RewardCentralProxy rewardCentralProxy;

	@Autowired
	TripPricerProxy tripPricerProxy;

	@Before
	public void init() {

		Locale.setDefault(Locale.ENGLISH);
		user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

	}

	@Test
	public void TEST_userGetRewards_WithOneAttractionShouldReturnResult() {
		rewardsService.setProximityBuffer(1);
		InternalTestHelper.setInternalUserNumber(0);
		
		
		Attraction attraction = gpsUtilProxy.recuperateTheAttraction().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		
		
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void TEST_userGetRewards_WithTwoActivities_ShouldReturnResult() {
		rewardsService.setProximityBuffer(1);

		InternalTestHelper.setInternalUserNumber(0);

		Attraction attraction = gpsUtilProxy.recuperateTheAttraction().get(0);
		Attraction attractionTwo = gpsUtilProxy.recuperateTheAttraction().get(1);

		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractionTwo, new Date()));

		tourGuideService.trackUserLocation(user); // Cette partie est extrmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}

	@Test
	public void TEST_userGetRewards_ForProximityBuffer() {
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);


		Double longitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);


		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));

		tourGuideService.trackUserLocation(user); // Cette partie est extrmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	
	@Test
	public void TEST_userGetRewards_ForTwoEquivalentPosition_ShouldReturnOnlyOneRewards() {
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);


		Double longitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;

		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));

		
		tourGuideService.trackUserLocation(user); // Cette partie est extrmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	
	@Test
	public void TEST_userGetRewards_ForTwoClosePosition_ShouldReturnTwoResults() {
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);


		Double longitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(2).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(2).latitude + 0.1;

		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));

		
		tourGuideService.trackUserLocation(user); // Cette partie est extrmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}

	
	@Test
	public void TEST_userGetRewards_ForFourClosePositionButEquivalent_ShouldReturnTwoResults() {
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);


		Double longitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;
		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(2).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtilProxy.recuperateTheAttraction().get(2).latitude + 0.1;
		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		Double longitudeNewPositionThree = gpsUtilProxy.recuperateTheAttraction().get(1).longitude + 0.1;
		Double lattitudeNewPositionThree = gpsUtilProxy.recuperateTheAttraction().get(1).latitude + 0.1;
		Location newLocThree = new Location(lattitudeNewPositionThree, longitudeNewPositionThree);
		
		Double longitudeNewPositionFour = gpsUtilProxy.recuperateTheAttraction().get(2).longitude + 0.1;
		Double lattitudeNewPositionFour = gpsUtilProxy.recuperateTheAttraction().get(2).latitude + 0.1;
		Location newLocFour = new Location(lattitudeNewPositionFour, longitudeNewPositionFour);
		
		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocThree, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocFour, new Date()));
		
		tourGuideService.trackUserLocation(user); // Cette partie est extrmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}
	
	
	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtilProxy.recuperateTheAttraction().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtilProxy.recuperateTheAttraction().size(), userRewards.size());
	}

}
