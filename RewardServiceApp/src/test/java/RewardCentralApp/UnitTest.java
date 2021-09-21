package RewardCentralApp;


import RewardCentralApp.service.RewardCentralService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTest {

    @Autowired
    RewardCentralService rewardCentralService;

    @Test
    public void getTheRewardFromService(){

        int result = rewardCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());

        assertTrue(result != 0);

    }
}
