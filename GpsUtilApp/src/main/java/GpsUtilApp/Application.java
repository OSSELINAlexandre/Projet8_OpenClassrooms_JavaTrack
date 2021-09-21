package GpsUtilApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableFeignClients
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
