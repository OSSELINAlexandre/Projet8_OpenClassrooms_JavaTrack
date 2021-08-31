package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

public class TestRewardsService {

	@Before
	public void init() {

		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void TEST_userGetRewards_WithOneAttractionShouldReturnResult() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(1);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void TEST_userGetRewards_WithTwoActivities_ShouldReturnResult() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(1);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		Attraction attractionTwo = gpsUtil.getAttractions().get(1);

		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractionTwo, new Date()));

		tourGuideService.trackUserLocation(user); // Cette partie est extrêmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}

	@Test
	public void TEST_userGetRewards_ForProximityBuffer() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		Double longitudeNewPosition = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtil.getAttractions().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);


		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));

		tourGuideService.trackUserLocation(user); // Cette partie est extrêmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	
	@Test
	public void TEST_userGetRewards_ForTwoEquivalentPosition_ShouldReturnOnlyOneRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		Double longitudeNewPosition = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtil.getAttractions().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtil.getAttractions().get(1).latitude + 0.1;

		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));

		
		tourGuideService.trackUserLocation(user); // Cette partie est extrêmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	
	@Test
	public void TEST_userGetRewards_ForTwoClosePosition_ShouldReturnTwoResults() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		Double longitudeNewPosition = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtil.getAttractions().get(1).latitude + 0.1;

		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtil.getAttractions().get(2).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtil.getAttractions().get(2).latitude + 0.1;

		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));

		
		tourGuideService.trackUserLocation(user); // Cette partie est extrêmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}

	
	@Test
	public void TEST_userGetRewards_ForFourClosePositionButEquivalent_ShouldReturnTwoResults() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(10);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		Double longitudeNewPosition = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPosition = gpsUtil.getAttractions().get(1).latitude + 0.1;
		Location newLoc = new Location(lattitudeNewPosition, longitudeNewPosition);
		
		Double longitudeNewPositionTwo = gpsUtil.getAttractions().get(2).longitude + 0.1;
		Double lattitudeNewPositionTwo = gpsUtil.getAttractions().get(2).latitude + 0.1;
		Location newLocTwo = new Location(lattitudeNewPositionTwo, longitudeNewPositionTwo);

		
		Double longitudeNewPositionThree = gpsUtil.getAttractions().get(1).longitude + 0.1;
		Double lattitudeNewPositionThree = gpsUtil.getAttractions().get(1).latitude + 0.1;
		Location newLocThree = new Location(lattitudeNewPositionThree, longitudeNewPositionThree);
		
		Double longitudeNewPositionFour = gpsUtil.getAttractions().get(2).longitude + 0.1;
		Double lattitudeNewPositionFour = gpsUtil.getAttractions().get(2).latitude + 0.1;
		Location newLocFour = new Location(lattitudeNewPositionFour, longitudeNewPositionFour);
		
		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLoc, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocTwo, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocThree, new Date()));
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), newLocFour, new Date()));
		
		tourGuideService.trackUserLocation(user); // Cette partie est extrêmement chronophage. plus de 30 seconds je
													// dirais facile.
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 2);
	}
	
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

}
