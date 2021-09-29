package TripPricerApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>TripPriceApp is our microservice that regroups all the logic for pricing.</p>
 * <p>This microservice is founded on a library provided by our client. (see libs/TripPricer.jar). </p>
 */
@SpringBootApplication
public class Application {

    public static ExecutorService executorService = Executors.newFixedThreadPool(3);


    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Application.class, args);
    }

}
