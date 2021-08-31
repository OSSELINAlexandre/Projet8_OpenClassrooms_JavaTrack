package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserNearbyAttraction;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		logger.info("Is this thing running ? I really don't know.");
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}	

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	public List<UserNearbyAttraction> getNearByFifthClosestAttractions(VisitedLocation visitedLocation) {
		List<UserNearbyAttraction> nearbyAttractions = new ArrayList<>();
		List<Attraction> resultAttraction = new ArrayList<>();

		Double distancedToBeChanged = 0.0;
		Integer indexToBeChange = null;

		for (Attraction attraction : gpsUtil.getAttractions()) {

			if (resultAttraction.size() < 5) {

				resultAttraction.add(attraction);

			} else {

				for (int i = 0; i < resultAttraction.size(); i++) {

					Location attractionLoc = new Location(attraction.latitude, attraction.longitude);

					Double attractionTestDist = rewardsService.getDistance(attractionLoc, visitedLocation.location);
					Double attractionBeingTested = rewardsService.getDistance(resultAttraction.get(i),
							visitedLocation.location);

					if (attractionTestDist < attractionBeingTested) {

						if (attractionBeingTested > distancedToBeChanged) {

							indexToBeChange = i;
							distancedToBeChanged = attractionBeingTested;

						}

					}
				}

				if (indexToBeChange != null) {

					resultAttraction.set(indexToBeChange, attraction);
					indexToBeChange = null;
					distancedToBeChanged = 0.0;

				}

			}

		}

		for (Attraction e : resultAttraction) {

			UserNearbyAttraction newItem = new UserNearbyAttraction();
			newItem.setUserLocation(visitedLocation.location);
			newItem.setTouristAttractionName(e.attractionName);
			Location attractionLoc = new Location(e.latitude, e.longitude);
			newItem.setAttractionLocation(attractionLoc);
			newItem.setDistanceInMilesBetweenUserAndAttraction(rewardsService.getDistance(e, visitedLocation.location));

			/**
			 * Think to see about the attractionRewardsPoint and send the user, not some
			 * null thing.
			 */
			newItem.setRewardsLinkedToTheAttraction(
					rewardsService.getRewardsCentral().getAttractionRewardPoints(null, null));
			nearbyAttractions.add(newItem);
		}

		return nearbyAttractions;
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
				logger.info("I've been called at the end of the program my dear");
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	public Map<String, User> getInternalUserMap() {
		return internalUserMap;
	}

	public void setInternalUserMap(Map<String, User> internalUserMap) {
		this.internalUserMap = internalUserMap;
	}
	
	

}
