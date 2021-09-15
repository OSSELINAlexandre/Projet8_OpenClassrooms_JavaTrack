package tourGuide.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.dto.gpsutildto.Attraction;
import tourGuide.dto.gpsutildto.Location;
import tourGuide.dto.gpsutildto.VisitedLocation;
import tourGuide.dto.trippricerdto.Provider;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserNearbyAttraction;
import tourGuide.user.UserReward;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TourGuideService {

	@Autowired
	GpsUtilProxy gpsUtilProxy;

	@Autowired
	TripPricerProxy tripPricerProxy;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final RewardsService rewardsService;
	public final static ReentrantLock lock = new ReentrantLock();
	public final Tracker tracker;
	boolean testMode = true;

	public TourGuideService(RewardsService rewardsService) {
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

		// Je vais tenter un trucc
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

	
	
	public RewardsService getRewardsService() {
		return rewardsService;
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricerProxy.getPrices(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {

		VisitedLocation visitedLocation = gpsUtilProxy.recuperateTheLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);

		
		logger.info("Is this thing running ? I really don't know.");
		rewardsService.calculateRewards(user);

		return visitedLocation;
	}

	public VisitedLocation trackUserLocationTestingPurposes(User user) {

		VisitedLocation visitedLocation = gpsUtilProxy.recuperateTheLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);

		logger.info("Is this thing running ? I really don't know. I think it is working pretty well man ahahha lets go to the cloub");
		rewardsService.calculateRewards(user);
		//I want to see if it can work properly
		return visitedLocation;
	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtilProxy.recuperateTheAttraction()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	public List<UserNearbyAttraction> getNearByFifthClosestAttractions(VisitedLocation visitedLocation, User u) {
		List<UserNearbyAttraction> nearbyAttractions = new ArrayList<>();
		List<Attraction> resultAttraction = new ArrayList<>();

		Double distancedToBeChanged = 0.0;
		Integer indexToBeChange = null;

		for (Attraction attraction : gpsUtilProxy.recuperateTheAttraction()) {

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
					rewardsService.getRewardPoints(e, u));
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
