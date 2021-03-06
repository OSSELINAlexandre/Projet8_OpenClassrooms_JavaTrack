package UserApp.UnitAndIntegration;

import UserApp.dto.UserPreferencesDTO;
import UserApp.model.User;
import UserApp.model.UserPreferences;
import UserApp.model.VisitedLocation;
import UserApp.service.UserService;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserFunctionalitiesUnitTest {

    @Autowired
    UserService userService;

    User user;

    @BeforeEach
    public void init(){
        String userName = "TestUser";
        String phone = "000";
        String email = userName + "@tourguide.com";
        user = new User(UUID.randomUUID(), userName, phone, email);
        user.addToVisitedLocations(new VisitedLocation());
        user.addToVisitedLocations(new VisitedLocation());
    }

    @Test
    public void Test_deleteAUser_ShouldReturnFalseIfUserDoesExists(){

        String userName = "TestUserDoesNotExists";
        String phone = "000";
        String email = userName + "@tourguide.com";
        User userTest = new User(UUID.randomUUID(), userName, phone, email);

        Boolean result = userService.deleteAUser(userTest.getUserName());

        assertFalse(result);

    }

    @Test
    public void Test_AddAUser(){

        User result = userService.addAUser(user);
        assertTrue(result != null);
    }

    @Test
    public void Test_deleteAUser_ShouldReturnTrueIfUserExists(){

        userService.users.add(user);

        Boolean result = userService.deleteAUser(user.getUserName());

        assertTrue(result);

    }



    @Test
    public void Test_updateUserPreferences(){

        userService.users.add(user);

        UserPreferencesDTO testingItem = new UserPreferencesDTO();
        testingItem.setLowerPricePoint(100);
        testingItem.setHighPricePoint(150);
        testingItem.setCurrency("EUR");
        testingItem.setAttractionProximity(150);
        testingItem.setTripDuration(10);
        testingItem.setNumberOfChildren(77);
        testingItem.setNumberOfAdults(25);
        testingItem.setTicketQuantity(450);

        User result = userService.updateUserPreferences(user.getUserName(), testingItem);

        assertTrue(result.getUserPreferences().getCurrency().getCurrencyCode().equals("EUR")
        && result.getUserPreferences().getTicketQuantity() == 450
        && result.getUserPreferences().getLowerPricePoint().getNumber().intValueExact() == 100);
    }

    @Test
    public void Test_getUserPreference(){
        UserPreferences result = user.getUserPreferences();

        assertTrue(result != null );

    }

    @Test
    public void Test_getUserRewards(){

        assertTrue(user.getUserRewards() != null);

    }

    @Test
    public void Test_getASpecificUser(){

        userService.users.add(user);
        User result = userService.getASpecificUser(user.getUserName());
        assertTrue(result != null);

    }

    @Ignore
    @Test
    public void Test_getAllUserLocationGivenUser(){

        userService.users.add(user);

        CopyOnWriteArrayList<VisitedLocation> result = userService.getAllUserLocationGivenUser(user.getUserName());

        assertTrue(result != null && result.size() == 2);


    }

}
