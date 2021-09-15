package RewardCentralApp;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService {

    private final RewardCentral rewardCentral;



    public RewardCentralService() {
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attraction, UUID user) {

        return rewardCentral.getAttractionRewardPoints(attraction, user);
    }
}
