package GpsUtilApp;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application implements ExitCodeGenerator {

    public static ExecutorService executorService = Executors.newFixedThreadPool(3);;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Application.class, args);
    }

    @Override
    public int getExitCode() {
        executorService.shutdown();
        return 0;
    }
}
