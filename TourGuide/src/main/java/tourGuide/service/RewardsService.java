package tourGuide.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.dto.gpsutildto.Attraction;
import tourGuide.dto.gpsutildto.Location;
import tourGuide.dto.gpsutildto.VisitedLocation;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardCentralProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;

@Service
public class RewardsService {

	@Autowired
	GpsUtilProxy gpsUtilProxy;

	@Autowired
	RewardCentralProxy rewardCentralProxy;

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	private Logger logger = LoggerFactory.getLogger(RewardsService.class);

	// proximity in miles used to be 10, switch to Max
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 10;
	

	public RewardsService() {
	}

	// For testing purposes, can be deleted afterwards

	public int getAttractionProximityRange() {
		return attractionProximityRange;
	}

	public void setAttractionProximityRange(int attractionProximityRange) {
		this.attractionProximityRange = attractionProximityRange;
	}

	// For testing purposes, can be deleted afterwards

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}



	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilProxy.recuperateTheAttraction();
		

		for (VisitedLocation visitedLocation : userLocations) {

			for (Attraction attraction : attractions) {
				/**
				 * Si dans l'ensemble des rewards recu par l'utilisateur l'attraction n'y est
				 * pas, alors / Ok, je pense que le but c'est de verifier par rapport a la
				 * localisation actuelle. Du coup, si une personne a deja visite une
				 * localisation, aucun nouveau point ne lui est donne.
				 */
				if (user.getUserRewards().stream()
						.filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						logger.info("DO WE GET THERE ? ");
						user.addUserReward(
								new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));

					}
				}
			}
		}
		
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardCentralProxy.getTheReward(attraction.attractionId, user.getUserId());
	}

	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

}
