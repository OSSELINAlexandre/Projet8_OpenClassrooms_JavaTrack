package UserApp;

import UserApp.dto.UserGpsDTO;
import UserApp.model.Attraction;
import UserApp.model.User;
import UserApp.model.UserNearbyAttraction;
import UserApp.model.VisitedLocation;
import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
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
public class GpsAppUnitTest {

    @Mock
    GpsUtilProxy gpsUtilProxy;

    @Mock
    RewardProxy rewardProxy;

    @Autowired
    UserService userService;

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

        userService.setGpsUtilProxy(gpsUtilProxy);
        userService.setRewardProxy(rewardProxy);

    }


    @Test
    public void unitTest_getUserLocation(){




        userService.users.add(user);

        UserGpsDTO testingItem = userService.transformUserIntoUserGpsDto(user);
        when(gpsUtilProxy.getTheLocation(any())).thenReturn(testingItem);

        VisitedLocation result = userService.getUserLocation(user.getUserName());

        assertTrue(result != null);

    }

    @Test
    public void unitTest_getAllAttraction(){


        when(gpsUtilProxy.getAllAttraction()).thenReturn(new ArrayList<Attraction>());

        List<Attraction> result = userService.getAllAttraction();

        assertTrue(result != null);

    }

    @Test
    public void unitTest_getAllFifthClosestAttraction(){



        userService.users.add(user);


        when(gpsUtilProxy.getFifthClosestAttraction(any())).thenReturn(new ArrayList<UserNearbyAttraction>());
        when(rewardProxy.getTheReward(any(), any())).thenReturn(5);

        List<UserNearbyAttraction> result = userService.getAllFifthClosestAttraction(user.getUserName());

        assertTrue(result != null);

    }


}
