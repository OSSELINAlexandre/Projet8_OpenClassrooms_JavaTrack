package UserApp;

import UserApp.proxy.GpsUtilProxy;
import UserApp.proxy.RewardProxy;
import UserApp.proxy.TripPricerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Locale;


/**
 * <b>The Application class is our main class for the Users application</b>
 *
 * <p>The Application class is annotated with EnableFeignClients and  SpringBootApplication</p>
 *
 * <ul>
 *     <li>SpringBootApplication is the annotation that construct the basic parameters for a spring boot application</li>
 *     <li>EnableFeignClients provide the application with the Feign library functionality easing the communication between microservices</li>
 * </ul>
 *
 */
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
