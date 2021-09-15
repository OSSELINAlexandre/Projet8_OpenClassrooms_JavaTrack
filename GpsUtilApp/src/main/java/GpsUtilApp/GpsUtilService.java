package GpsUtilApp;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {

    private final GpsUtil gpsUtil;

    public GpsUtilService(){
        gpsUtil = new GpsUtil();
    }


    public VisitedLocation getTheUser(UUID theId) {

        return gpsUtil.getUserLocation(theId);
    }

    public List<Attraction> getAllAttraction() {

        return gpsUtil.getAttractions();
    }
}
