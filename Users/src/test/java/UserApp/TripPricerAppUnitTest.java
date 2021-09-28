package UserApp;

import UserApp.model.Provider;
import UserApp.model.User;
import UserApp.model.VisitedLocation;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class TripPricerAppUnitTest {

    @Autowired
    UserService userService;

    @Mock
    TripPricerProxy tripPricerProxy;

    User user;

    @BeforeEach
    public void init(){
        userService.setTheProfileTrueForTestFalseForExperience(true);
        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        user = new User(UUID.randomUUID(), userName, phone, email);
        user.addToVisitedLocations(new VisitedLocation());
        user.addToVisitedLocations(new VisitedLocation());

        userService.setTripPricerProxy(tripPricerProxy);

    }

    @Test
    public void getAllTheDeals(){

        userService.users.add(user);


        when(tripPricerProxy.getPrices(any(String.class), any(UUID.class), any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(new ArrayList<Provider>());
        List<Provider> result = userService.getAllTheDeals(user.getUserName());

        assertTrue(result != null);


    }
}
