package RewardCentralApp.dto;

import RewardCentralApp.model.Attraction;
import RewardCentralApp.model.User;

import java.util.List;
import java.util.UUID;

public class UserAndAttractionDTO extends User {

    private List<Attraction> attractions;

    public UserAndAttractionDTO() {
    }

    public UserAndAttractionDTO(UUID userId, String userName, String phoneNumber, String emailAddress) {
        super(userId, userName, phoneNumber, emailAddress);
    }


    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }
}
