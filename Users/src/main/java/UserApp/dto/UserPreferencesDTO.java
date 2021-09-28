package UserApp.dto;

/**
 * <p>UserPreferencesDTO is used in order to facilitate the modification of user's preferences</p>
 * <p>Indeed, the JavaMoney library provided by the client is a complex type, especially for transfer of information between microservices.</p>
 * <p>Solely few information's of the UserPreferences are needed for the API in charge of the pricing.</p>
 * <p>Therefore, this class facilitate the posting of new preferences for the client of the application. </p>
 * <p>Plus, it asks only the required information for the API in charge of the pricing (in our case, TripApp).</p>
 */
public class UserPreferencesDTO {


    /*
    *
    * To see which currency can be implemented, please go to :
    *
    * http://apps.ncsc.org/niem/schemas/iso_4217.html
    *
    * */
    private int attractionProximity;
    private String currency;
    private int lowerPricePoint;
    private int highPricePoint;
    private int tripDuration;
    private int ticketQuantity;
    private int numberOfAdults;
    private int numberOfChildren;

    public UserPreferencesDTO() {
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(int lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public int getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(int highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
