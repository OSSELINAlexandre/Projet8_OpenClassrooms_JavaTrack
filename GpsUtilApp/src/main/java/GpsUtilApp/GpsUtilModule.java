package GpsUtilApp;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

/**
 * <p>GpsUtilModule is the class centralizing all the beans required for the spring IoC to function properly.</p>
 */
@Configuration
public class GpsUtilModule {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new MoneyModule());
    }

}
