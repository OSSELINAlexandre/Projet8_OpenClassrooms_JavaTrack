package UserApp;

import UserApp.model.Provider;
import UserApp.model.User;
import UserApp.model.VisitedLocation;
import UserApp.proxy.TripPricerProxy;
import UserApp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripPricerAppUnitTest {

    @Autowired
    UserService userService;

    @Mock
    TripPricerProxy tripPricerProxy;

    User user;

    @Before
    public void init(){
        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        user = new User(UUID.randomUUID(), userName, phone, email);
        user.addToVisitedLocations(new VisitedLocation());
        user.addToVisitedLocations(new VisitedLocation());
    }

    @Test
    public void getAllTheDeals(){

        userService.users.add(user);

        userService.setTripPricerProxy(tripPricerProxy);

        when(tripPricerProxy.getPrices(any(String.class), any(UUID.class), any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(new ArrayList<Provider>());
        List<Provider> result = userService.getAllTheDeals(user.getUserName());

        assertTrue(result != null);


    }
}
