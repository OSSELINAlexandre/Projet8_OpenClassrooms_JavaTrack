package TripPricerApplication;

import TripPricerApplication.service.TripPricerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class UnitTest {

    @Autowired
    TripPricerService tripPricerService;

    @Test
    public void getTheListOfTrip(){

        List<Provider> result = tripPricerService.getTheList("test", UUID.randomUUID(), 7 , 7 , 7 , 777);
        assertTrue(result.size() == 5);

    }

}
