package UserApp;

import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.proxy.TripPricerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Locale;

@EnableFeignClients
@SpringBootApplication
public class Application {

    /*
    *
    * We put these two autowied proxy in order for the proxy to be initialized before the service, and that the tracker can be automatically lauch
    *
    *
    * */

    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    @Autowired
    RewardProxy rewardProxy;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Application.class, args);

    }
}
