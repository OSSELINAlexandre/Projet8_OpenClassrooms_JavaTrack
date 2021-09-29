package GpsUtilApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;


/**
 * <p>GpsUtilApp is our microservice that regroups all the logic for localization.</p>
 * <p>This microservice is founded on a library provided by our client. (see libs/GpsUtil.jar). </p>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Application.class, args);
    }

}
