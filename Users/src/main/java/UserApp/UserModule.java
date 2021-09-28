package UserApp;


import UserApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

/**
 * <p>The UserModule class is the class that configure some beans needed for the IoC to function properly.</p>
 */
@Configuration
public class UserModule {

    @Bean
    public UserService getUserService(){
        return new UserService();
    }

    /**
     * This bean enable the JavaMoney library (see gradle.build) to be used (this library was provided by the client).
     *
     */
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new MoneyModule());
    }


}
